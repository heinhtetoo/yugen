package com.yugen.animeapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yugen.animeapp.data.local.dao.AnimeDao
import com.yugen.animeapp.data.local.dao.AnimeGenreDao
import com.yugen.animeapp.data.local.dao.SearchHistoryDao
import com.yugen.animeapp.data.local.entity.AnimeGenreEntity
import com.yugen.animeapp.data.local.entity.SearchHistoryEntity
import com.yugen.animeapp.data.mapper.toAnime
import com.yugen.animeapp.data.mapper.toAnimeDetails
import com.yugen.animeapp.data.mapper.toAnimeEntity
import com.yugen.animeapp.data.mapper.toAnimeGenre
import com.yugen.animeapp.data.mapper.toAnimeGenreCrossRefEntityList
import com.yugen.animeapp.data.mapper.toAnimeGenreEntity
import com.yugen.animeapp.data.remote.api.AnimePagingSource
import com.yugen.animeapp.data.remote.api.JikanApiService
import com.yugen.animeapp.data.remote.model.AnimeGenreResponse
import com.yugen.animeapp.data.remote.model.AnimeResponse
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.AnimeDetails
import com.yugen.animeapp.domain.model.AnimeGenre
import com.yugen.animeapp.domain.model.DefaultHomeSectionType
import com.yugen.animeapp.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApiService,
    private val animeDao: AnimeDao,
    private val genreDao: AnimeGenreDao,
    private val searchDao: SearchHistoryDao
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

    override fun getAnimeGenreById(genreId: Int): Flow<AnimeGenre?> =
        genreDao.getAnimeGenreById(genreId).map { it.toAnimeGenre() }

    override fun getDefaultAnimeListByType(type: DefaultHomeSectionType): Flow<List<Anime>> =
        animeDao.getAnimeListByGenreId(type.genreId)
            .map { list -> list.map { it.toAnime() } }

    override suspend fun refreshDefaultAnimeListByType(type: DefaultHomeSectionType) {
        if (type.filterEnumString != null) {
            val systemGenre = AnimeGenreEntity(
                id = type.genreId,
                name = type.name,
                count = null
            )

            genreDao.upsertAnimeGenres(listOf(systemGenre))

            val response = api.fetchTopAnimeByFilterEnum(filter = type.filterEnumString, page = 1)
            val list = response.data ?: emptyList()

            val customList: List<AnimeResponse> = list.map {
                AnimeResponse(
                    id = it.id,
                    images = it.images,
                    title = it.title,
                    status = it.status,
                    synopsis = it.synopsis,
                    genres = listOf(
                        AnimeGenreResponse(
                            id = type.genreId,
                            name = type.name,
                            url = ""
                        )
                    )
                )
            }

            animeDao.refreshAnimeListWithGenreLinks(
                list = customList.map { it.toAnimeEntity() },
                links = customList.flatMap { it.toAnimeGenreCrossRefEntityList() }
            )
        } else refreshAnimeListByGenreId(type.genreId)
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

    override fun searchPagedAnime(
        genreId: Int?,
        type: String?,
        status: String?,
        query: String
    ): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                AnimePagingSource(
                    api,
                    animeGenreId = genreId,
                    searchQuery = query,
                    animeType = type,
                    animeStatus = status
                )
            }
        ).flow

    override fun getRecentSearches(): Flow<List<String>> =
        searchDao.getRecentSearches().map { list -> list.map { it.query } }

    override suspend fun addSearchToHistory(query: String) =
        searchDao.insertSearch(SearchHistoryEntity(query))

    override suspend fun removeSearchFromHistory(query: String) =
        searchDao.deleteSearch(query)

    override suspend fun clearAllSearches() =
        searchDao.clearAll()

    override fun getAnimeDetailsById(animeId: Int): Flow<AnimeDetails?> =
        animeDao.getAnimeDetailsByAnimeId(animeId)
            .map { entity -> entity?.toAnimeDetails() }

    override suspend fun fetchAnimeDetailsById(animeId: Int) {
        val response = api.fetchAnimeDetailsById(animeId)
        val details = response.data

        details?.let { animeDao.upsertAnime(it.toAnimeEntity()) }
    }

    override suspend fun fetchAnimeRecommendationsById(animeId: Int): List<Anime> =
        try {
            val response = api.fetchAnimeRecommendationsById(animeId)
            val list = response.data?.take(15) ?: emptyList()

            list.map { it.toAnime() }
        } catch (e: Exception) {
            emptyList()
        }
}