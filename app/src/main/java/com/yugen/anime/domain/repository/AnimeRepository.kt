package com.yugen.anime.domain.repository

import androidx.paging.PagingData
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import com.yugen.anime.domain.model.AnimeGenre
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    fun getAnimeGenres(): Flow<List<AnimeGenre>>
    suspend fun refreshAnimeGenresIfNecessary()

    fun getAnimeListByGenreId(genreId: Int): Flow<List<Anime>>
    suspend fun refreshAnimeListByGenreId(genreId: Int)

    fun getPagedAnimeListByGenreId(genreId: Int): Flow<PagingData<Anime>>

    fun searchPagedAnime(genreId: Int? = null, query: String): Flow<PagingData<Anime>>

    fun getAnimeDetailsById(animeId: Int): Flow<AnimeDetails?>
    suspend fun fetchAnimeDetailsById(animeId: Int)
}