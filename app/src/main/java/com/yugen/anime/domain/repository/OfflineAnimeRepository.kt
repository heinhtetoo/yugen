package com.yugen.anime.domain.repository

import com.yugen.anime.data.local.entities.AnimeEntity
import kotlinx.coroutines.flow.Flow

interface OfflineAnimeRepository {

    fun getAllLocalAnimeStream(): Flow<List<AnimeEntity>>

    fun getLocalAnimeStream(id: Int): Flow<AnimeEntity?>

    suspend fun insertLocalAnime(animeEntity: AnimeEntity)

    suspend fun updateLocalAnime(animeEntity: AnimeEntity)

    suspend fun deleteLocalAnime(animeEntity: AnimeEntity)
}