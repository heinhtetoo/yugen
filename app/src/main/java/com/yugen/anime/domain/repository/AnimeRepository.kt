package com.yugen.anime.domain.repository

import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    fun getTopAnime(): Flow<List<Anime>>
    suspend fun refreshTopAnime()

    fun getAwardWinningAnime(): Flow<List<Anime>>
    suspend fun refreshAwardWinningAnime()

    fun getFantasyAnime(): Flow<List<Anime>>
    suspend fun refreshFantasyAnime()

    fun getAnimeDetailsById(id: Int): Flow<AnimeDetails?>
    suspend fun fetchAnimeDetailsById(
        id: Int,
        isFavourite: Boolean,
        isTop: Boolean,
        isAwardWinning: Boolean,
        isFantasy: Boolean
    )
}