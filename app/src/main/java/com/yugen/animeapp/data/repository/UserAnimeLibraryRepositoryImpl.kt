package com.yugen.animeapp.data.repository

import com.yugen.animeapp.data.local.dao.UserAnimeLibraryDao
import com.yugen.animeapp.data.local.entities.UserAnimeLibraryEntity
import com.yugen.animeapp.data.mapper.toAnime
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.WatchStatus
import com.yugen.animeapp.domain.repository.UserAnimeLibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserAnimeLibraryRepositoryImpl @Inject constructor(
    private val animeLibraryDao: UserAnimeLibraryDao
) : UserAnimeLibraryRepository {

    override fun getUserAnimeLibraryByStatus(status: WatchStatus): Flow<List<Anime>> =
        animeLibraryDao.getUserAnimeLibraryByStatus(status).map { it.map { anime -> anime.toAnime() } }

    override suspend fun setAnimeWatchStatus(
        animeId: Int,
        status: WatchStatus
    ) {
        animeLibraryDao.upsertAnimeLibraryEntity(UserAnimeLibraryEntity(animeId, status))
    }

    override suspend fun removeAnimeFromLibrary(animeId: Int) =
        animeLibraryDao.removeFromLibrary(animeId)

    override fun getAnimeWatchStatus(animeId: Int): Flow<WatchStatus?> =
        animeLibraryDao.getAnimeWatchStatus(animeId)
}