package com.yugen.anime.domain.repository

import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    fun getTopAnime(): Flow<List<Anime>>

    fun getAnimeDetailsById(id: Int): Flow<AnimeDetails?>

    suspend fun refreshTopAnime()

    suspend fun fetchAnimeDetailsById(id: Int, isTop: Boolean, isFavourite: Boolean)
}