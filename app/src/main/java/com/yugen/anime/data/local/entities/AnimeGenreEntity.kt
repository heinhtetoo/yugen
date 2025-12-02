package com.yugen.anime.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "anime_genre_listings",
    primaryKeys = ["genreId", "animeId"],
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
data class AnimeGenreEntity(
    val genreId: Int,
    val genreName: String,
    val animeId: Int,
    val position: Int
)