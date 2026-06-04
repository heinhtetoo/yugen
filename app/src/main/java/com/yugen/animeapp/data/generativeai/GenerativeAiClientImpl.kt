package com.yugen.animeapp.data.generativeai

import android.util.Log
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.yugen.animeapp.BuildConfig
import com.yugen.animeapp.core.utils.GENERATIVE_MODEL_NAME
import com.yugen.animeapp.core.utils.GEN_AI_SYSTEM_INSTRUCTION
import com.yugen.animeapp.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Production implementation of [GenerativeAiClient] using Google's Gemini model.
 * Manages chat session lifecycle and handles streaming message responses.
 */
@Singleton
class GenerativeAiClientImpl @Inject constructor() : GenerativeAiClient {

    private val generativeModel = GenerativeModel(
        modelName = GENERATIVE_MODEL_NAME,
        apiKey = BuildConfig.GEMINI_API_KEY,
        systemInstruction = content {
            text(GEN_AI_SYSTEM_INSTRUCTION)
        }
    )

    private var chatSession: Chat? = null

    override suspend fun initializeSession(history: List<ChatMessage>) {
        val contentHistory = history.map { message ->
            content(role = if (message.isUser) "user" else "model") { text(message.text) }
        }
        chatSession = generativeModel.startChat(history = contentHistory)
    }

    override suspend fun sendMessage(userMessage: String): Flow<String> = flow {
        try {
            check(chatSession != null) { "Chat session not initialized. Call initializeSession first." }

            val responseStream = chatSession!!.sendMessageStream(userMessage)

            responseStream.collect { chunk ->
                val text = chunk.text ?: ""
                emit(text)
            }
        } catch (e: Exception) {
            resetSession()
            Log.e(TAG, "Error sending message: ${e.localizedMessage}", e)
            throw e
        }
    }

    override suspend fun resetSession() {
        chatSession = null
    }

    companion object {
        private const val TAG = "GenerativeAiClientImpl"
    }
}

