package com.eyeshield.sharewise.core.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharedFlow
import java.net.Socket

interface ClientSocketRepository {

    val clientScope: CoroutineScope
        get() = CoroutineScope(Dispatchers.IO + SupervisorJob())

    var client: Socket
    val messages: SharedFlow<String>

    fun connect()
    suspend fun sendMessage(message: String)
    suspend fun receiveMessage()
}