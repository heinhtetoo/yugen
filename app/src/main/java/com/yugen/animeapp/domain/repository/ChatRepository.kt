package com.yugen.animeapp.domain.repository

import com.yugen.animeapp.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun initSession()
    fun getAllMessages(): Flow<List<ChatMessage>>
    suspend fun sendMessage(userMessage: String): Flow<String>
    suspend fun clearHistory()
}