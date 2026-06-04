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
import com.yugen.animeapp.data.local.dao.RemoteKeysDao
import com.yugen.animeapp.data.local.dao.SearchHistoryDao
import com.yugen.animeapp.data.local.entity.AnimeEntity
import com.yugen.animeapp.data.local.entity.AnimeGenreCrossRefEntity
import com.yugen.animeapp.data.local.entity.AnimeGenreEntity
import com.yugen.animeapp.data.local.entity.AnimeRemoteKeys
import com.yugen.animeapp.data.local.entity.ChatMessageEntity
import com.yugen.animeapp.data.local.entity.FavouriteAnimeEntity
import com.yugen.animeapp.data.local.entity.SearchHistoryEntity
import com.yugen.animeapp.data.local.entity.UserAnimeLibraryEntity

@Database(
    entities = [
        AnimeEntity::class,
        AnimeGenreEntity::class,
        AnimeGenreCrossRefEntity::class,
        FavouriteAnimeEntity::class,
        UserAnimeLibraryEntity::class,
        SearchHistoryEntity::class,
        ChatMessageEntity::class,
        AnimeRemoteKeys::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class YugenDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao
    abstract fun animeGenreDao(): AnimeGenreDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

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