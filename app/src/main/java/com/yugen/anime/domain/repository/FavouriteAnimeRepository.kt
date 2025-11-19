package com.yugen.anime.domain.repository

import com.yugen.anime.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface FavouriteAnimeRepository {

    fun getFavouriteAnime(): Flow<List<Anime>>

    fun isFavourite(id: Int): Flow<Boolean>

    suspend fun addFavouriteAnime(id: Int)

    suspend fun removeFavouriteAnime(id: Int)
}