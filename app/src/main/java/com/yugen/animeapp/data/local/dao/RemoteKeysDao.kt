package com.yugen.animeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yugen.animeapp.data.local.entity.AnimeRemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<AnimeRemoteKeys>)

    @Query("SELECT * FROM anime_remote_keys WHERE animeId = :animeId AND genreId = :genreId")
    suspend fun remoteKeysAnimeId(animeId: Int, genreId: Int): AnimeRemoteKeys?

    @Query("DELETE FROM anime_remote_keys WHERE genreId = :genreId")
    suspend fun clearRemoteKeys(genreId: Int)
}