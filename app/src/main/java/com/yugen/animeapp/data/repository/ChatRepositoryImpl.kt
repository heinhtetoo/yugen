package com.yugen.animeapp.data.repository

import android.util.Log
import com.yugen.animeapp.data.generativeai.GenerativeAiClient
import com.yugen.animeapp.data.local.dao.ChatDao
import com.yugen.animeapp.data.local.entity.ChatMessageEntity
import com.yugen.animeapp.data.mapper.toChatMessage
import com.yugen.animeapp.domain.model.ChatMessage
import com.yugen.animeapp.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val generativeAiClient: GenerativeAiClient
) : ChatRepository {

    override suspend fun initSession() {
        val history = getAllMessages().first()
        generativeAiClient.initializeSession(history)
    }

    override fun getAllMessages(): Flow<List<ChatMessage>> =
        chatDao.getAllMessages().map { list -> list.map { it.toChatMessage() } }

    override suspend fun sendMessage(userMessage: String): Flow<String> = flow {
        chatDao.insertMessage(ChatMessageEntity(text = userMessage, isUser = true))

        if (chatDao.getAllMessages().first().isEmpty()) {
            // Initialize session on first message
            generativeAiClient.initializeSession(emptyList())
        }

        try {
            val fullResponseBuilder = StringBuilder()

            generativeAiClient.sendMessage(userMessage).collect { chunk ->
                fullResponseBuilder.append(chunk)
                emit(chunk)
            }

            chatDao.insertMessage(
                ChatMessageEntity(
                    text = fullResponseBuilder.toString(),
                    isUser = false
                )
            )
        } catch (e: Exception) {
            emit("Error: ${e.localizedMessage}")
            Log.e("ChatRepositoryImpl", "Error during message send: ${e.localizedMessage}", e)
            chatDao.insertMessage(
                ChatMessageEntity(
                    text = "Sorry, I encountered an error.",
                    isUser = false,
                    isError = true
                )
            )
        }
    }

    override suspend fun clearHistory() = chatDao.clearHistory()

    override suspend fun resetSession() {
        generativeAiClient.resetSession()
    }
}