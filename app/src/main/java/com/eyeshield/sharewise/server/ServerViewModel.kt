package com.eyeshield.sharewise.server

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.sharewise.core.domain.ServerSocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.Socket
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor(
    private val serverSocketRepository: ServerSocketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(
        UiState()
    )
    val uiState = _uiState.asStateFlow()


    init {
        serverSocketRepository.startServer()

        viewModelScope.launch {
            serverSocketRepository._messages.collect { message ->
                _uiState.update {
                    it.copy(
                        message = message
                    )
                }
            }
        }

        viewModelScope.launch {
            serverSocketRepository.availableClients.collect { clients ->
                _uiState.update {
                    it.copy(
                        clients = clients
                    )
                }
                Timber.tag("VIJ").d("Updated list of clients: ${clients}")
            }
        }
    }


    data class UiState(
        val message: String = "",
        val clients: Map<Socket, String> = mapOf()
    )
}