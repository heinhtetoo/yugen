package com.yugen.animeapp.di

import com.yugen.animeapp.data.repository.AnimeRepositoryImpl
import com.yugen.animeapp.data.repository.LibraryRepositoryImpl
import com.yugen.animeapp.data.repository.UserPreferencesRepositoryImpl
import com.yugen.animeapp.domain.repository.AnimeRepository
import com.yugen.animeapp.domain.repository.LibraryRepository
import com.yugen.animeapp.domain.repository.UserPreferencesRepository
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
    abstract fun bindAnimeRepository(impl: AnimeRepositoryImpl): AnimeRepository

    @Binds
    @Singleton
    abstract fun bindLibraryRepository(impl: LibraryRepositoryImpl): LibraryRepository

//    @Binds
//    @Singleton
//    abstract fun bindJikanRepository(jikanRepositoryImpl: JikanRepositoryImpl): JikanRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesDataStore(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository
}