package com.yugen.anime.di

import com.yugen.anime.data.repository.AnimeRepositoryImpl
import com.yugen.anime.data.repository.FavouriteAnimeRepositoryImpl
import com.yugen.anime.data.repository.JikanRepositoryImpl
import com.yugen.anime.domain.repository.AnimeRepository
import com.yugen.anime.domain.repository.FavouriteAnimeRepository
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
    abstract fun bindAnimeRepository(impl: AnimeRepositoryImpl): AnimeRepository

    @Binds
    @Singleton
    abstract fun bindFavouriteAnimeRepository(impl: FavouriteAnimeRepositoryImpl): FavouriteAnimeRepository

//    @Binds
//    @Singleton
//    abstract fun bindJikanRepository(jikanRepositoryImpl: JikanRepositoryImpl): JikanRepository
}