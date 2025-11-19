package com.yugen.anime.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yugen.anime.data.local.dao.AnimeDao
import com.yugen.anime.data.local.dao.FavouriteAnimeDao
import com.yugen.anime.data.local.entities.AnimeEntity
import com.yugen.anime.data.local.entities.FavouriteAnimeEntity

@Database(entities = [AnimeEntity::class, FavouriteAnimeEntity::class], version = 1, exportSchema = false)
abstract class YugenDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao
    abstract fun favouriteAnimeDao(): FavouriteAnimeDao

    companion object {

        @Volatile
        private var Instance: YugenDatabase? = null

        fun getDatabase(context: Context): YugenDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, YugenDatabase::class.java, "yugen_database")
                    .fallbackToDestructiveMigration(false).build().also {
                        Instance = it
                    }
            }
        }
    }
}