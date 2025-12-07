package com.yugen.anime.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.yugen.anime.data.local.entities.AnimeGenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeGenreDao {

    @Upsert
    suspend fun upsertAnimeGenres(animeGenres: List<AnimeGenreEntity>)

    @Query("SELECT * FROM anime_genres ORDER BY count DESC")
    fun getAnimeGenres(): Flow<List<AnimeGenreEntity>>

    @Query("SELECT * FROM anime_genres WHERE id = :id")
    fun getAnimeGenreById(id: Int): Flow<AnimeGenreEntity>

    @Query("SELECT COUNT(*) FROM anime_genres")
    suspend fun getGenreCount(): Int
}