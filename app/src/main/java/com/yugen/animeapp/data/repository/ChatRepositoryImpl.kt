package com.yugen.animeapp.data.repository

import android.util.Log
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.yugen.animeapp.BuildConfig
import com.yugen.animeapp.core.utils.GENERATIVE_MODEL_NAME
import com.yugen.animeapp.core.utils.GEN_AI_SYSTEM_INSTRUCTION
import com.yugen.animeapp.data.local.dao.ChatDao
import com.yugen.animeapp.data.local.entities.ChatMessageEntity
import com.yugen.animeapp.data.mapper.toChatMessage
import com.yugen.animeapp.domain.model.ChatMessage
import com.yugen.animeapp.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao
) : ChatRepository {

    private val generativeModel = GenerativeModel(
        modelName = GENERATIVE_MODEL_NAME,
        apiKey = BuildConfig.GEMINI_API_KEY,
        systemInstruction = content {
            text(GEN_AI_SYSTEM_INSTRUCTION)
        }
    )

    private var chatSession: Chat? = null

    override suspend fun initSession() {
        val history = getAllMessages().first()
            .map { message ->
                content(role = if (message.isUser) "user" else "model") { text(message.text) }
            }

        chatSession = generativeModel.startChat(history = history)
    }

    override fun getAllMessages(): Flow<List<ChatMessage>> =
        chatDao.getAllMessages().map { list -> list.map { it.toChatMessage() } }

    override suspend fun sendMessage(userMessage: String): Flow<String> = flow {
        chatDao.insertMessage(ChatMessageEntity(text = userMessage, isUser = true))

        if (chatSession == null) initSession()

        try {
            val responseStream = chatSession!!.sendMessageStream(userMessage)

            val fullResponseBuilder = StringBuilder()

            responseStream.collect { chunk ->
                val text = chunk.text ?: ""
                fullResponseBuilder.append(text)
                emit(text)
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
            chatSession = null  // Reset session so it re-initialises on next message
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
        chatSession = null
    }
}