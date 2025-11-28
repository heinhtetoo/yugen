package com.yugen.anime.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yugen.anime.data.local.dao.AnimeDao
import com.yugen.anime.data.mapper.toAnime
import com.yugen.anime.data.mapper.toAnimeDetails
import com.yugen.anime.data.mapper.toAnimeEntity
import com.yugen.anime.data.remote.api.AnimePagingSource
import com.yugen.anime.data.remote.api.JikanApiService
import com.yugen.anime.data.remote.model.AnimeResponse
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import com.yugen.anime.domain.model.AnimeSource
import com.yugen.anime.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApiService,
    private val dao: AnimeDao
) : AnimeRepository {

    override fun getTopAiringAnime(): Flow<List<Anime>> =
        dao.getTopAiringAnime()
            .map { list -> list.map { it.toAnime() } }

    override suspend fun refreshTopAiringAnime() {
        val response = api.fetchTopAnime(filter = "airing", page = 1)
        val list = response.data ?: emptyList()
        dao.insertAnimeList(list.map { it.toAnimeEntity(isTopAiring = true) })
    }

    override fun getTopUpcomingAnime(): Flow<List<Anime>> =
        dao.getTopUpcomingAnime()
            .map { list -> list.map { it.toAnime() } }

    override suspend fun refreshTopUpcomingAnime() {
        val response = api.fetchTopAnime(filter = "upcoming", page = 1)
        val list = response.data ?: emptyList()
        dao.insertAnimeList(list.map { it.toAnimeEntity(isTopUpcoming = true) })
    }

    override fun getAwardWinningAnime(): Flow<List<Anime>> =
        dao.getAwardWinningAnime()
            .map { list -> list.map { it.toAnime() } }

    // TODO:: Update instead of Insert
    override suspend fun refreshAwardWinningAnime() {
        val response = api.fetchAnimeListByGenreId(genreId = 42, page = 1)
        val list = response.data ?: emptyList()
        dao.insertAnimeList(list.map { it.toAnimeEntity(isAwardWinning = true) })
    }

    override fun getFantasyAnime(): Flow<List<Anime>> =
        dao.getFantasyAnime()
            .map { list -> list.map { it.toAnime() } }

    // TODO:: Update instead of Insert
    override suspend fun refreshFantasyAnime() {
        val response = api.fetchAnimeListByGenreId(genreId = 10, page = 1)
        val list = response.data ?: emptyList()
        dao.insertAnimeList(list.map { it.toAnimeEntity(isFantasy = true) })
    }

    override fun getPagedAnime(source: AnimeSource): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AnimePagingSource(api, source) }
        ).flow

    override fun getAnimeDetailsById(id: Int): Flow<AnimeDetails?> =
        dao.getAnimeDetailsById(id)
            .map { entity -> entity?.toAnimeDetails() }

    override suspend fun fetchAnimeDetailsById(
        id: Int,
        isFavourite: Boolean,
        isTopAiring: Boolean,
        isTopUpcoming: Boolean,
        isAwardWinning: Boolean,
        isFantasy: Boolean
    ) {
        val response = api.getAnimeById(id)
        val details = response.data

        details?.let {
            dao.insertAnime(
                it.toAnimeEntity(
                    isFavourite,
                    isTopAiring,
                    isTopUpcoming,
                    isAwardWinning,
                    isFantasy
                )
            )
        }
    }
}