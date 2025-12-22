package com.yugen.animeapp.domain.repository

import com.yugen.animeapp.data.local.model.LibraryItem
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.WatchStatus
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    fun getUserAnimeLibraryItems(): Flow<List<LibraryItem>>
    fun getFavouriteAnime(): Flow<List<Anime>>
    suspend fun addFavouriteAnime(animeId: Int)
    suspend fun removeFavouriteAnime(animeId: Int)
    fun isFavouriteAnime(animeId: Int): Flow<Boolean>
    suspend fun setAnimeWatchStatus(animeId: Int, status: WatchStatus)
    suspend fun removeAnimeFromLibrary(animeId: Int)
    fun getAnimeWatchStatus(animeId: Int): Flow<WatchStatus?>
}