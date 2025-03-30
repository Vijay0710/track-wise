package com.eyeshield.sharewise.core.data

import com.eyeshield.sharewise.core.domain.ClientSocketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.Socket
import javax.inject.Inject

class ClientSocketRepositoryImpl @Inject constructor(
    private val serverIp: String,
    private val port: Int
) : ClientSocketRepository {

    override lateinit var client: Socket

    private val _messages = MutableSharedFlow<String>()

    override val messages: SharedFlow<String>
        get() = _messages.asSharedFlow()

    override fun connect() {
        if (!::client.isInitialized || client.isClosed) {
            clientScope.launch {
                try {
                    client = Socket(serverIp, port)
                    Timber.tag("VIJ").d("Connected to server: $serverIp:$port")

                    launch {
                        receiveMessage()
                    }
                } catch (e: Exception) {
                    Timber.tag("VIJ").d("Failed to connect: ${e.message}")
                }
            }
        }
    }

    private suspend fun reconnect() {
        Timber.tag("VIJ").d("Reconnecting...")
        client.close() // Close the old connection if needed
        delay(1000) // Short delay before reconnecting
        connect()
    }


    override suspend fun sendMessage(message: String) {
        withContext(Dispatchers.IO) {
            try {
                if (client.isConnected) {
                    client.outputStream.apply {
                        write("$message\n".toByteArray())
                        flush()
                    }
                    Timber.tag("VIJ").d("Sent: $message")
                } else {
                    Timber.tag("VIJ").d("Client is disconnected")
                }
            } catch (e: Exception) {
                Timber.tag("VIJ").e("Send failed: ${e.message}")
                reconnect() // Try reconnecting if sending fails
            }
        }
    }

    override suspend fun receiveMessage() {
        val reader = client.inputStream.bufferedReader()
        try {
            while (true) {
                val message = reader.readLine() ?: return
                _messages.emit(message) // Emit message to UI
            }
        } catch (e: Exception) {
            Timber.tag("VIJ").d("Disconnected from server")
        }
    }
}