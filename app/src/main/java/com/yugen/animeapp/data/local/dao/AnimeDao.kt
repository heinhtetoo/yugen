package com.yugen.animeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.yugen.animeapp.data.local.entity.AnimeGenreCrossRefEntity
import com.yugen.animeapp.data.local.entity.AnimeEntity
import com.yugen.animeapp.data.local.model.AnimeItem
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
        INNER JOIN anime_genre_cross_refs c ON a.id = c.animeId
        WHERE c.genreId = :genreId
        ORDER BY c.dateAdded DESC
        """
    )
    fun getAnimeListByGenreId(genreId: Int): Flow<List<AnimeItem>>

    @Query(
        """
        SELECT
            a.*
            ,(f.animeId IS NOT NULL) AS isFavourite
        FROM anime a
        LEFT JOIN favourite_anime f ON a.id = f.animeId
        LEFT JOIN anime_genre_cross_refs c ON a.id = c.animeId
        WHERE a.id = :animeId
        """
    )
    fun getAnimeDetailsByAnimeId(animeId: Int): Flow<AnimeItem?>

    @Upsert
    suspend fun upsertAnime(anime: AnimeEntity)

    @Upsert
    suspend fun upsertAnimeList(list: List<AnimeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreLinks(links: List<AnimeGenreCrossRefEntity>)

    @Query("DELETE FROM anime_genre_cross_refs WHERE genreId = :genreId")
    suspend fun deleteGenreLinksByGenreId(genreId: Int)

    @Query("DELETE FROM anime_genre_cross_refs WHERE genreId IN (:genreIds)")
    suspend fun deleteGenreLinksByGenreIds(genreIds: List<Int>)

    @Query("DELETE FROM anime_genre_cross_refs WHERE animeId = :animeId")
    suspend fun deleteGenreLinksByAnimeId(animeId: Int)

    @Query("SELECT MAX(position) FROM anime_genre_cross_refs WHERE genreId = :genreId")
    suspend fun getMaxPositionForGenre(genreId: Int): Int?

    @Transaction
    suspend fun refreshAnimeListWithGenreLinks(
        list: List<AnimeEntity>,
        links: List<AnimeGenreCrossRefEntity>
    ) {
        val newIds = list.map { it.id }
        val localMap = getAnimeSubsetByIds(newIds).associateBy { it.id }
        val mergedList = list.map { remoteAnime ->
            val localAnime = localMap[remoteAnime.id]
            if (localAnime != null && !localAnime.type.isNullOrEmpty()) {
                remoteAnime.copy(
                    titleEnglish = localAnime.titleEnglish,
                    titleJapanese = localAnime.titleJapanese,
                    type = localAnime.type,
                    episodes = localAnime.episodes,
                    rating = localAnime.rating
                )
            } else {
                remoteAnime
            }
        }

        upsertAnimeList(mergedList)
        mergedList.forEach { anime -> deleteGenreLinksByAnimeId(anime.id) }
        insertGenreLinks(links)
    }

    // TODO :: Make this private??
    @Query("SELECT * FROM anime WHERE id IN (:ids)")
    suspend fun getAnimeSubsetByIds(ids: List<Int>): List<AnimeEntity>
}