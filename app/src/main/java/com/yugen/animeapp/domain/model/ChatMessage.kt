package com.yugen.animeapp.domain.model

data class ChatMessage(
    val id: Long,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long,
    val isError: Boolean
)
