package com.yugen.animeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.yugen.animeapp.data.local.entities.FavouriteAnimeEntity
import com.yugen.animeapp.data.local.entities.UserAnimeLibraryEntity
import com.yugen.animeapp.data.local.model.AnimeItem
import com.yugen.animeapp.data.local.model.GenreStat
import com.yugen.animeapp.data.local.model.LibraryItem
import com.yugen.animeapp.data.local.model.WatchStatusCount
import com.yugen.animeapp.domain.model.WatchStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {

    @Query(
        """
        SELECT 
            a.*
            ,(f.animeId IS NOT NULL) AS isFavourite
        FROM anime a
        INNER JOIN favourite_anime f ON a.id = f.animeId
        ORDER BY f.dateAdded DESC
        """
    )
    fun getFavouriteAnime(): Flow<List<AnimeItem>>

    @Query(
        """
        SELECT 
            DISTINCT a.*
            ,(f.animeId IS NOT NULL) AS isFavourite
        FROM anime a
        INNER JOIN favourite_anime f ON a.id = f.animeId
        INNER JOIN anime_genre_cross_refs c ON a.id = c.animeId
        WHERE c.genreId IN (:genreIds)
        """
    )
    fun getFavouriteAnimeByGenreIds(genreIds: List<Int>): Flow<List<AnimeItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteAnime(favouriteAnimeEntity: FavouriteAnimeEntity)

    @Query("DELETE FROM favourite_anime WHERE animeId = :animeId")
    suspend fun deleteFavouriteAnime(animeId: Int)

    @Query("SELECT EXISTS (SELECT 1 FROM favourite_anime WHERE animeId = :animeId)")
    fun isFavouriteAnime(animeId: Int): Flow<Boolean>

    @Upsert
    suspend fun upsertAnimeLibraryEntity(entity: UserAnimeLibraryEntity)

    @Query("DELETE FROM user_anime_library WHERE animeId = :animeId")
    suspend fun removeFromLibrary(animeId: Int)

    @Query("SELECT status FROM user_anime_library WHERE animeId = :animeId")
    fun getAnimeWatchStatus(animeId: Int): Flow<WatchStatus?>

    @Transaction
    @Query(
        """
        SELECT 
            a.*,
            l.status AS watchStatus,
            CASE WHEN f.animeId IS NOT NULL THEN 1 ELSE 0 END AS isFavourite
        FROM anime a
        LEFT JOIN user_anime_library l ON a.id = l.animeId
        LEFT JOIN favourite_anime f ON a.id = f.animeId
        WHERE l.status IS NOT NULL OR f.animeId IS NOT NULL
        ORDER BY l.lastUpdated DESC
        """
    )
    fun getUserAnimeLibraryItems(): Flow<List<LibraryItem>>

    @Query("SELECT status AS statusId, COUNT(*) AS count FROM user_anime_library GROUP BY status")
    fun getWatchStatusCounts(): Flow<List<WatchStatusCount>>

    @Query(
        """
        SELECT 
            g.name AS genreName, 
            COUNT(c.animeId) AS count
        FROM user_anime_library l
        INNER JOIN anime_genre_cross_refs c ON l.animeId = c.animeId
        INNER JOIN anime_genres g ON c.genreId = g.id
        GROUP BY g.id
        ORDER BY count DESC
        LIMIT 5
        """
    )
    fun getTopGenresInLibrary(): Flow<List<GenreStat>>
}