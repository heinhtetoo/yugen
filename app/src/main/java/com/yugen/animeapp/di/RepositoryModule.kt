package com.yugen.animeapp.di

import com.yugen.animeapp.data.repository.AnimeRepositoryImpl
import com.yugen.animeapp.data.repository.FavouriteAnimeRepositoryImpl
import com.yugen.animeapp.data.repository.UserAnimeLibraryRepositoryImpl
import com.yugen.animeapp.data.repository.UserPreferencesRepositoryImpl
import com.yugen.animeapp.domain.repository.AnimeRepository
import com.yugen.animeapp.domain.repository.FavouriteAnimeRepository
import com.yugen.animeapp.domain.repository.UserAnimeLibraryRepository
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
    abstract fun bindFavouriteAnimeRepository(impl: FavouriteAnimeRepositoryImpl): FavouriteAnimeRepository

    @Binds
    @Singleton
    abstract fun bindUserAnimeLibraryRepository(impl: UserAnimeLibraryRepositoryImpl): UserAnimeLibraryRepository

//    @Binds
//    @Singleton
//    abstract fun bindJikanRepository(jikanRepositoryImpl: JikanRepositoryImpl): JikanRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesDataStore(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository
}