package com.yugen.animeapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yugen.animeapp.domain.model.WatchStatus

@Entity(
    tableName = "user_anime_library",
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["animeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["animeId"])]
)
data class UserAnimeLibraryEntity(
    @PrimaryKey val animeId: Int,
    val status: WatchStatus,
    val lastUpdated: Long = System.currentTimeMillis()
)