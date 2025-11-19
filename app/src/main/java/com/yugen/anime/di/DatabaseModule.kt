package com.yugen.anime.di

import android.content.Context
import androidx.room.Room
import com.yugen.anime.data.local.YugenDatabase
import com.yugen.anime.data.local.dao.AnimeDao
import com.yugen.anime.data.local.dao.FavouriteAnimeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): YugenDatabase =
        Room.databaseBuilder(
            context,
            YugenDatabase::class.java,
            "yugen.db"
        ).fallbackToDestructiveMigration(false).build()

    @Provides
    fun provideAnimeDao(db: YugenDatabase): AnimeDao = db.animeDao()

    @Provides
    fun provideFavouriteAnimeDao(db: YugenDatabase): FavouriteAnimeDao = db.favouriteAnimeDao()
}