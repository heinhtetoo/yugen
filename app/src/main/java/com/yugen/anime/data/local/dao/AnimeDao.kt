package com.yugen.anime.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yugen.anime.data.local.entities.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Query("SELECT * FROM anime WHERE isTopAiring = 1")
    fun getTopAiringAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE isTopUpcoming = 1")
    fun getTopUpcomingAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE isAwardWinning = 1")
    fun getAwardWinningAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE isFantasy = 1")
    fun getFantasyAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE id = :id")
    fun getAnimeDetailsById(id: Int): Flow<AnimeEntity?>

    @Query("SELECT * FROM anime WHERE id IN (:ids)")
    fun getAnimeListByIds(ids: List<Int>): Flow<List<AnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(list: List<AnimeEntity>)
}