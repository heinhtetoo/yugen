package com.yugen.anime.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yugen.anime.data.local.dao.AnimeDao
import com.yugen.anime.data.local.dao.AnimeGenreDao
import com.yugen.anime.data.mapper.toAnime
import com.yugen.anime.data.mapper.toAnimeDetails
import com.yugen.anime.data.mapper.toAnimeEntity
import com.yugen.anime.data.mapper.toAnimeGenre
import com.yugen.anime.data.mapper.toAnimeGenreCrossRefEntityList
import com.yugen.anime.data.mapper.toAnimeGenreEntity
import com.yugen.anime.data.remote.api.AnimePagingSource
import com.yugen.anime.data.remote.api.JikanApiService
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import com.yugen.anime.domain.model.AnimeGenre
import com.yugen.anime.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApiService,
    private val animeDao: AnimeDao,
    private val genreDao: AnimeGenreDao
) : AnimeRepository {

    override fun getAnimeGenres(): Flow<List<AnimeGenre>> =
        genreDao.getAnimeGenres()
            .map { list -> list.map { it.toAnimeGenre() } }

    override suspend fun refreshAnimeGenresIfNecessary() {
        val count = genreDao.getGenreCount()

        if (count == 0) {
            try {
                val response = api.fetchAnimeGenres()
                val list = response.data ?: emptyList()

                genreDao.upsertAnimeGenres(
                    animeGenres = list.map { it.toAnimeGenreEntity() }
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getAnimeListByGenreId(genreId: Int): Flow<List<Anime>> =
        animeDao.getAnimeListByGenreId(genreId)
            .map { list -> list.map { it.toAnime() } }

    override suspend fun refreshAnimeListByGenreId(genreId: Int) {
        val response = api.fetchAnimeListByGenreId(genreId = genreId, page = 1)
        val list = response.data ?: emptyList()

        animeDao.refreshAnimeListWithGenreLinks(
            list = list.map { it.toAnimeEntity() },
            links = list.flatMap { it.toAnimeGenreCrossRefEntityList() }
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

    override fun getPagedAnimeListByGenreId(genreId: Int): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AnimePagingSource(api, genreId) }
        ).flow

    override fun searchPagedAnime(genreId: Int?, query: String): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AnimePagingSource(api, genreId, query) }
        ).flow

    override fun getAnimeDetailsById(animeId: Int): Flow<AnimeDetails?> =
        animeDao.getAnimeDetailsByAnimeId(animeId)
            .map { entity -> entity?.toAnimeDetails() }

    override suspend fun fetchAnimeDetailsById(animeId: Int) {
        val response = api.getAnimeById(animeId)
        val details = response.data

        details?.let { animeDao.upsertAnime(it.toAnimeEntity()) }
    }
}