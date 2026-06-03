package com.yugen.animeapp.ui.screen.chat

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.animeapp.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    val messages = chatRepository.getAllMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _streamingResponse = MutableStateFlow("")
    val streamingResponse = _streamingResponse.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch { chatRepository.initSession() }
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || _isLoading.value) return

        _isLoading.value = true
        _streamingResponse.value = ""

        viewModelScope.launch {
            chatRepository.sendMessage(text)
                .onCompletion {
                    _isLoading.value = false
                    _streamingResponse.value = ""
                }
                .collect { chunk ->
                    _streamingResponse.value += chunk
                }
        }
    }
}