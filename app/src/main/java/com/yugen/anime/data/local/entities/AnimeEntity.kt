package com.yugen.anime.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey
    val id: Int,
    val title: String? = "",
    val titleEnglish: String? = "",
    val titleJapanese: String? = "",
    val status: String? = "",
    val type: String? = "",
    val episodes: Int = 0,
    val rating: String? = "",
    val synopsis: String? = "",
    val isTop: Boolean = false,
    val isFavourite: Boolean = false
)
