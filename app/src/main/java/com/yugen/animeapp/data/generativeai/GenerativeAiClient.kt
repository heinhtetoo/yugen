package com.yugen.animeapp.data.generativeai

import com.yugen.animeapp.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for Google Generative AI (Gemini) client.
 * Handles chat session management and message streaming.
 */
interface GenerativeAiClient {
    /**
     * Initialize or reset the chat session with optional conversation history.
     */
    suspend fun initializeSession(history: List<ChatMessage>)

    /**
     * Send a message and receive streaming response chunks.
     * @param userMessage The user's message to send
     * @return Flow of text chunks from the model's response
     */
    suspend fun sendMessage(userMessage: String): Flow<String>

    /**
     * Reset the current chat session.
     * Next [sendMessage] call will reinitialise the session.
     */
    suspend fun resetSession()
}

