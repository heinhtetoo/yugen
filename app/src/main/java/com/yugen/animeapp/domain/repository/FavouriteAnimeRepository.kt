package com.yugen.animeapp.domain.repository

import com.yugen.animeapp.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface FavouriteAnimeRepository {

    fun getFavouriteAnime(): Flow<List<Anime>>
    suspend fun addFavouriteAnime(animeId: Int)
    suspend fun removeFavouriteAnime(animeId: Int)
    fun isFavouriteAnime(animeId: Int): Flow<Boolean>
}