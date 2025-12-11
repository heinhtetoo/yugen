//package com.yugen.anime.data.local.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.yugen.anime.data.local.entities.AnimeEntity
//import com.yugen.anime.data.local.entities.FavouriteAnimeEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface FavouriteAnimeDao {
//
//    @Query("SELECT animeId FROM favourite_anime")
//    fun getFavouriteAnimeIds(): Flow<List<Int>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertFavouriteAnime(favouriteAnimeEntity: FavouriteAnimeEntity)
//
//    @Query("DELETE FROM favourite_anime WHERE animeId = :animeId")
//    suspend fun deleteFavouriteAnime(animeId: Int)
//
//    @Query("SELECT EXISTS(SELECT 1 FROM favourite_anime WHERE animeId = :animeId)")
//    fun isFavourite(animeId: Int): Flow<Boolean>
//}