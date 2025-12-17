package com.yugen.animeapp.domain.repository

import androidx.paging.PagingData
import com.yugen.animeapp.data.local.entities.AnimeEntity
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.AnimeDetails
import com.yugen.animeapp.domain.model.AnimeGenre
import com.yugen.animeapp.domain.model.DefaultHomeSectionType
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    fun getAnimeGenres(): Flow<List<AnimeGenre>>
    suspend fun refreshAnimeGenresIfNecessary()

    fun getAnimeGenreById(genreId: Int): Flow<AnimeGenre?>

    fun getDefaultAnimeListByType(type: DefaultHomeSectionType): Flow<List<Anime>>
    suspend fun refreshDefaultAnimeListByType(type: DefaultHomeSectionType)

    fun getAnimeListByGenreId(genreId: Int): Flow<List<Anime>>
    suspend fun refreshAnimeListByGenreId(genreId: Int)

    fun getPagedAnimeListByGenreId(genreId: Int): Flow<PagingData<Anime>>

    fun searchPagedAnime(genreId: Int? = null, query: String): Flow<PagingData<Anime>>

    fun getAnimeDetailsById(animeId: Int): Flow<AnimeDetails?>
    suspend fun fetchAnimeDetailsById(animeId: Int)

    suspend fun fetchAnimeRecommendationsById(animeId: Int): List<Anime>
}