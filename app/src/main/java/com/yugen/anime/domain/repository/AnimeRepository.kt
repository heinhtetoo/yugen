package com.yugen.anime.domain.repository

import androidx.paging.PagingData
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import com.yugen.anime.domain.model.AnimeCategory
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

    fun getPagedAnimeList(category: AnimeCategory): Flow<PagingData<Anime>>

    fun searchPagedAnime(category: AnimeCategory? = null, query: String): Flow<PagingData<Anime>>

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