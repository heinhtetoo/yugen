package com.yugen.animeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.yugen.animeapp.data.local.entities.AnimeEntityWrapper
import com.yugen.animeapp.data.local.entities.UserAnimeLibraryEntity
import com.yugen.animeapp.domain.model.WatchStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAnimeLibraryDao {

    @Upsert
    suspend fun upsertAnimeLibraryEntity(entity: UserAnimeLibraryEntity)

    @Query("DELETE FROM user_anime_library WHERE animeId = :animeId")
    suspend fun removeFromLibrary(animeId: Int)

    @Query("SELECT status FROM user_anime_library WHERE animeId = :animeId")
    fun getAnimeWatchStatus(animeId: Int): Flow<WatchStatus?>

    @Transaction
    @Query("""
        SELECT 
            a.*
            ,(f.animeId IS NOT NULL) AS isFavourite
        FROM anime a
        INNER JOIN user_anime_library l ON a.id == l.animeId
        INNER JOIN favourite_anime f ON a.id == f.animeId
        WHERE l.status = :status
        ORDER BY l.lastUpdated DESC
        """
    )
    fun getUserAnimeLibraryByStatus(status: WatchStatus): Flow<List<AnimeEntityWrapper>>
}