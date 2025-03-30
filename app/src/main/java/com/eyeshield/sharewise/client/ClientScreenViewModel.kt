package com.eyeshield.sharewise.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.sharewise.core.domain.ClientSocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientScreenViewModel @Inject constructor(
    private val clientSocketRepository: ClientSocketRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<UiState>(UiState())
    val messages = _messages.asStateFlow()

    private fun sendMessage(message: String) {
        viewModelScope.launch {
            clientSocketRepository.sendMessage(message)
        }
    }

    init {
        viewModelScope.launch {
            clientSocketRepository.connect()
        }
        observeAndUpdateMessage()
    }

    private fun observeAndUpdateMessage() {
        viewModelScope.launch {
            clientSocketRepository.messages.collect { message ->
                _messages.update {
                    it.copy(
                        message = message
                    )
                }
            }
        }
    }

    fun onUiAction(action: UiAction) {
        when (action) {
            is UiAction.SendMessage -> {
                sendMessage(action.message)
            }
        }
    }

    sealed interface UiAction {
        data class SendMessage(val message: String) : UiAction
    }

    data class UiState(
        val message: String = ""
    )
}