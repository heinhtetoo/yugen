package com.yugen.anime.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.yugen.anime.data.local.YugenDatabase
import com.yugen.anime.data.local.dao.AnimeDao
import com.yugen.anime.data.mapper.toAnime
import com.yugen.anime.data.mapper.toAnimeDetails
import com.yugen.anime.data.mapper.toAnimeEntity
import com.yugen.anime.data.mapper.toAnimeGenreEntityList
import com.yugen.anime.data.remote.api.AnimePagingSource
import com.yugen.anime.data.remote.api.JikanApiService
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import com.yugen.anime.domain.model.AnimeGenre
import com.yugen.anime.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApiService,
    private val dao: AnimeDao
) : AnimeRepository {

    override fun getAnimeListByGenreId(genreId: Int): Flow<List<Anime>> =
        dao.getAnimeListByGenreId(genreId)
            .map { list -> list.map { it.toAnime() } }

    override suspend fun refreshAnimeListByGenreId(genreId: Int) {
        val response = api.fetchAnimeListByGenreId(genreId = genreId, page = 1)
        val list = response.data ?: emptyList()

        dao.refreshAnimeListWithGenreLinks(
            list = list.map { it.toAnimeEntity() },
            links = list.flatMap { it.toAnimeGenreEntityList() }
        )
    }

//    override fun getTopUpcomingAnime(): Flow<List<Anime>> =
//        dao.getTopUpcomingAnime()
//            .map { list -> list.map { it.toAnime() } }
//
//
    // TODO:: Update instead of Insert
//    override suspend fun refreshTopUpcomingAnime() {
//        val response = api.fetchTopAnime(filter = "upcoming", page = 1)
//        val list = response.data ?: emptyList()
//        dao.insertAnimeList(list.map { it.toAnimeEntity(isTopUpcoming = true) })
//    }

    override fun getPagedAnimeListByGenreId(animeGenre: AnimeGenre): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AnimePagingSource(api, animeGenre) }
        ).flow

    override fun searchPagedAnime(animeGenre: AnimeGenre?, query: String): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AnimePagingSource(api, animeGenre, query) }
        ).flow

    override fun getAnimeDetailsById(animeId: Int): Flow<AnimeDetails?> =
        dao.getAnimeDetailsByAnimeId(animeId)
            .map { entity -> entity?.toAnimeDetails() }

    override suspend fun fetchAnimeDetailsById(animeId: Int) {
        val response = api.getAnimeById(animeId)
        val details = response.data

        details?.let { dao.upsertAnime(it.toAnimeEntity()) }
    }
}