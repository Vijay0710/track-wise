package com.eyeshield.sharewise.core.data

import com.eyeshield.sharewise.core.domain.ServerSocketRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class ServerSocketRepositoryImpl @Inject constructor(
    private val port: Int,
) : ServerSocketRepository {

    override lateinit var serverSocket: ServerSocket

    override val _messages: MutableSharedFlow<String> = MutableSharedFlow()

    val clients: ConcurrentHashMap<Socket, String> = ConcurrentHashMap()

    override val availableClients = MutableStateFlow<ConcurrentHashMap<Socket, String>>(
        ConcurrentHashMap()
    )


    override fun startServer() {
        serverScope.launch {
            serverSocket = ServerSocket()
            serverSocket.bind(InetSocketAddress("0.0.0.0", port))

            Timber.tag("VIJ")
                .d("Server Socket is started: ${serverSocket.localSocketAddress} - ${serverSocket.localPort}")

            while (true) {
                val clientSocket = serverSocket.accept()

                Timber.tag("VIJ").d("Server Side, Client Socket is ${clientSocket.inetAddress}")

                clientSocket?.let {
                    launch {
                        addClient(clientSocket)
                        handleClient(clientSocket)
                    }
                }
            }
        }
    }

    private fun addClient(clientSocket: Socket) {
        val clientAddress = clientSocket.inetAddress.hostAddress
        clients[clientSocket] = clientAddress!!

        availableClients.update {
            val newAvailableClients = ConcurrentHashMap<Socket, String>()
            newAvailableClients.apply {
                put(clientSocket, clientAddress)
            }
        }
    }

    private fun removeClient(client: Socket) {
        clients.remove(client)
        availableClients.update {
            availableClients.value.apply {
                val newAvailableClients = ConcurrentHashMap<Socket, String>().apply { putAll(it) }
                newAvailableClients.remove(client)
                newAvailableClients
            }
        }
        client.close()
    }

    override fun stopServer() {
        serverScope.cancel()
        serverSocket.close()
        clients.forEach { (socket, hostAddress) ->
            socket.close()
        }
        clients.clear()
    }

    private suspend fun handleClient(client: Socket) {
        val reader = client.inputStream.bufferedReader()

        try {
            while (true) {
                val message = reader.readLine() ?: break
                Timber.tag("VIJ")
                    .d("Received in server from client ${client.localSocketAddress}: $message")
                _messages.emit(message)
                broadcastMessage(message, client)
            }
        } catch (e: Exception) {
            Timber.tag("VIJ").d("Client disconnected: ${e.message + client.localSocketAddress}")
        } finally {
            Timber.tag("VIJ")
                .d("In finally blocked closed all socket connections and cleared clients")
            removeClient(client)
            clients.clear()
        }
    }

    private fun broadcastMessage(message: String, sender: Socket) {
        serverScope.launch {
            clients.forEach { (client, hostAddress) ->
                if (sender != client) {
                    val outputStream: OutputStream = client.outputStream
                    outputStream.write("$message\n".toByteArray())
                    outputStream.flush()
                }
            }
        }
    }
}