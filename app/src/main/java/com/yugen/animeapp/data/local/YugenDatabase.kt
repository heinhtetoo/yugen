package com.yugen.animeapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yugen.animeapp.core.utils.Converters
import com.yugen.animeapp.data.local.dao.AnimeDao
import com.yugen.animeapp.data.local.dao.AnimeGenreDao
import com.yugen.animeapp.data.local.dao.ChatDao
import com.yugen.animeapp.data.local.dao.LibraryDao
import com.yugen.animeapp.data.local.dao.SearchHistoryDao
import com.yugen.animeapp.data.local.entities.AnimeEntity
import com.yugen.animeapp.data.local.entities.AnimeGenreCrossRefEntity
import com.yugen.animeapp.data.local.entities.AnimeGenreEntity
import com.yugen.animeapp.data.local.entities.ChatMessageEntity
import com.yugen.animeapp.data.local.entities.FavouriteAnimeEntity
import com.yugen.animeapp.data.local.entities.SearchHistoryEntity
import com.yugen.animeapp.data.local.entities.UserAnimeLibraryEntity

@Database(
    entities = [
        AnimeEntity::class,
        AnimeGenreEntity::class,
        AnimeGenreCrossRefEntity::class,
        FavouriteAnimeEntity::class,
        UserAnimeLibraryEntity::class,
        SearchHistoryEntity::class,
        ChatMessageEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class YugenDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao
    abstract fun animeGenreDao(): AnimeGenreDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    //    abstract fun favouriteAnimeDao(): FavouriteAnimeDao
    abstract fun userAnimeLibraryDao(): LibraryDao
    abstract fun chatDao(): ChatDao

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