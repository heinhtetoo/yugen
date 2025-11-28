package com.yugen.anime.domain.repository

import androidx.paging.PagingData
import com.yugen.anime.data.remote.model.AnimeResponse
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import com.yugen.anime.domain.model.AnimeSource
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    fun getTopAiringAnime(): Flow<List<Anime>>
    suspend fun refreshTopAiringAnime()

    fun getTopUpcomingAnime(): Flow<List<Anime>>
    suspend fun refreshTopUpcomingAnime()

    fun getAwardWinningAnime(): Flow<List<Anime>>
    suspend fun refreshAwardWinningAnime()

    fun getFantasyAnime(): Flow<List<Anime>>
    suspend fun refreshFantasyAnime()

    fun getPagedAnime(source: AnimeSource): Flow<PagingData<Anime>>

    fun getAnimeDetailsById(id: Int): Flow<AnimeDetails?>
    suspend fun fetchAnimeDetailsById(
        id: Int,
        isFavourite: Boolean,
        isTopAiring: Boolean,
        isTopUpcoming: Boolean,
        isAwardWinning: Boolean,
        isFantasy: Boolean
    )
}