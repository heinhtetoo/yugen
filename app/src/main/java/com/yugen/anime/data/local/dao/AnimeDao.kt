package com.yugen.anime.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.yugen.anime.data.local.entities.AnimeGenreEntity
import com.yugen.anime.data.local.entities.AnimeEntity
import com.yugen.anime.data.local.entities.AnimeEntityWrapper
import com.yugen.anime.data.local.entities.FavouriteAnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Query(
        """
        SELECT 
            a.* 
            ,(f.animeId IS NOT NULL) AS isFavourite
        FROM anime a
        LEFT JOIN favourite_anime f ON a.id = f.animeId
        INNER JOIN anime_genre_listings g ON a.id = g.animeId
        WHERE g.genreId = :genreId
        ORDER BY g.position ASC
        """
    )
    fun getAnimeListByGenreId(genreId: Int): Flow<List<AnimeEntityWrapper>>

    @Query("""
        SELECT
            a.*
            ,(f.animeId IS NOT NULL) AS isFavourite
        FROM anime a
        LEFT JOIN favourite_anime f ON a.id = f.animeId
        LEFT JOIN anime_genre_listings g ON a.id = g.animeId
        WHERE a.id = :animeId
        """
    )
    fun getAnimeDetailsByAnimeId(animeId: Int): Flow<AnimeEntityWrapper?>

    @Upsert
    suspend fun upsertAnime(anime: AnimeEntity)

    @Upsert
    suspend fun upsertAnimeList(list: List<AnimeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreLinks(links: List<AnimeGenreEntity>)

    @Query("DELETE FROM anime_genre_listings WHERE genreId IN (:genreIds)")
    suspend fun deleteGenreLinks(genreIds: List<Int>)

    @Query("DELETE FROM anime_genre_listings WHERE animeId = :animeId")
    suspend fun deleteGenreLinksByAnimeId(animeId: Int)

    // TODO :: Update only necessary columns instead of Upsert
    @Transaction
    suspend fun refreshAnimeListWithGenreLinks(list: List<AnimeEntity>, links: List<AnimeGenreEntity>) {
        upsertAnimeList(list)
        list.forEach { anime ->
            deleteGenreLinksByAnimeId(anime.id)
        }
        insertGenreLinks(links)
    }

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
    fun getFavouriteAnime(): Flow<List<AnimeEntityWrapper>>

    @Query("""
        SELECT 
            DISTINCT a.*
            ,(f.animeId IS NOT NULL) AS isFavourite
        FROM anime a
        INNER JOIN favourite_anime f ON a.id = f.animeId
        INNER JOIN anime_genre_listings g ON a.id = g.animeId
        WHERE g.genreId IN (:genreIds)
        """
    )
    fun getFavouriteAnimeByGenreIds(genreIds: List<Int>): Flow<List<AnimeEntityWrapper>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteAnime(favouriteAnimeEntity: FavouriteAnimeEntity)

    @Query("DELETE FROM favourite_anime WHERE animeId = :animeId")
    suspend fun deleteFavouriteAnime(animeId: Int)

    @Query("SELECT EXISTS (SELECT 1 FROM favourite_anime WHERE animeId = :animeId)")
    fun isFavouriteAnime(animeId: Int): Flow<Boolean>
}