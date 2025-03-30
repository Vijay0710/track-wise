package com.eyeshield.sharewise.core.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap

interface ServerSocketRepository {

    val _messages: MutableSharedFlow<String>

    var serverSocket: ServerSocket

    val availableClients: MutableStateFlow<ConcurrentHashMap<Socket, String>>

    val serverScope: CoroutineScope
        get() = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun startServer()

    fun stopServer()
}