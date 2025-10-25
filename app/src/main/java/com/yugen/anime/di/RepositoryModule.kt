package com.yugen.anime.di

import com.yugen.anime.data.repository.JikanRepositoryImpl
import com.yugen.anime.domain.repository.JikanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindJikanRepository(jikanRepositoryImpl: JikanRepositoryImpl): JikanRepository
}