package com.yugen.animeapp.data.repository

import com.yugen.animeapp.data.local.dao.LibraryDao
import com.yugen.animeapp.data.local.entity.FavouriteAnimeEntity
import com.yugen.animeapp.data.local.entity.UserAnimeLibraryEntity
import com.yugen.animeapp.data.local.model.GenreStat
import com.yugen.animeapp.data.local.model.LibraryItem
import com.yugen.animeapp.data.local.model.WatchStatusCount
import com.yugen.animeapp.data.mapper.toAnime
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.WatchStatus
import com.yugen.animeapp.domain.repository.LibraryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class LibraryRepositoryImpl @Inject constructor(
    private val libraryDao: LibraryDao
) : LibraryRepository {

    override fun getUserAnimeLibraryItems(): Flow<List<LibraryItem>> =
        libraryDao.getUserAnimeLibraryItems()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavouriteAnime(): Flow<List<Anime>> =
        libraryDao.getFavouriteAnime().map { it.map { anime -> anime.toAnime() } }

    override suspend fun addFavouriteAnime(animeId: Int) {
        libraryDao.insertFavouriteAnime(FavouriteAnimeEntity(animeId = animeId))
    }

    override suspend fun removeFavouriteAnime(animeId: Int) {
        libraryDao.deleteFavouriteAnime(animeId = animeId)
    }

    override fun isFavouriteAnime(animeId: Int): Flow<Boolean> =
        libraryDao.isFavouriteAnime(animeId)

    override suspend fun setAnimeWatchStatus(
        animeId: Int,
        status: WatchStatus
    ) {
        libraryDao.upsertAnimeLibraryEntity(UserAnimeLibraryEntity(animeId, status))
    }

    override suspend fun removeAnimeFromLibrary(animeId: Int) =
        libraryDao.removeFromLibrary(animeId)

    override fun getAnimeWatchStatus(animeId: Int): Flow<WatchStatus?> =
        libraryDao.getAnimeWatchStatus(animeId)

    override fun getLibraryWatchStatusCounts(): Flow<List<WatchStatusCount>> =
        libraryDao.getWatchStatusCounts()

    override fun getTopLibraryGenres(): Flow<List<GenreStat>> =
        libraryDao.getTopGenresInLibrary()
}