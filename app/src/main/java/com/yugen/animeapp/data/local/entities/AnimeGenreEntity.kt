package com.yugen.animeapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_genres")
data class AnimeGenreEntity(
    @PrimaryKey val id: Int,
    val type: String? = null,
    val name: String = "",
    val count: Int? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)
