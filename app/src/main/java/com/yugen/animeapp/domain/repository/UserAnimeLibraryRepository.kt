package com.yugen.animeapp.domain.repository

import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.WatchStatus
import kotlinx.coroutines.flow.Flow

interface UserAnimeLibraryRepository {

    fun getUserAnimeLibraryByStatus(status: WatchStatus): Flow<List<Anime>>
    suspend fun setAnimeWatchStatus(animeId: Int, status: WatchStatus)
    suspend fun removeAnimeFromLibrary(animeId: Int)
    fun getAnimeWatchStatus(animeId: Int): Flow<WatchStatus?>
}