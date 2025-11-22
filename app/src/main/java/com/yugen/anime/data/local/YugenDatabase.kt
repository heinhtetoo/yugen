package com.yugen.anime.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.yugen.anime.core.utils.Converters
import com.yugen.anime.data.local.dao.AnimeDao
import com.yugen.anime.data.local.dao.FavouriteAnimeDao
import com.yugen.anime.data.local.entities.AnimeEntity
import com.yugen.anime.data.local.entities.FavouriteAnimeEntity

@Database(entities = [AnimeEntity::class, FavouriteAnimeEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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