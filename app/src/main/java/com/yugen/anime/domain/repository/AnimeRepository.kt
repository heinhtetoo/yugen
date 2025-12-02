package com.yugen.anime.domain.repository

import androidx.paging.PagingData
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import com.yugen.anime.domain.model.AnimeGenre
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    fun getAnimeListByGenreId(genreId: Int): Flow<List<Anime>>
    suspend fun refreshAnimeListByGenreId(genreId: Int)

    fun getPagedAnimeListByGenreId(animeGenre: AnimeGenre): Flow<PagingData<Anime>>

    fun searchPagedAnime(animeGenre: AnimeGenre? = null, query: String): Flow<PagingData<Anime>>

    fun getAnimeDetailsById(animeId: Int): Flow<AnimeDetails?>
    suspend fun fetchAnimeDetailsById(animeId: Int)
}