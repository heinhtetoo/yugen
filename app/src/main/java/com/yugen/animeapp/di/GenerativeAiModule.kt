package com.yugen.animeapp.di

import com.yugen.animeapp.data.generativeai.GenerativeAiClient
import com.yugen.animeapp.data.generativeai.GenerativeAiClientImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing GenerativeAI (Gemini) dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class GenerativeAiModule {

    @Binds
    @Singleton
    abstract fun bindGenerativeAiClient(impl: GenerativeAiClientImpl): GenerativeAiClient
}

