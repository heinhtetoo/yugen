package com.yugen.anime.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yugen.anime.domain.model.Images

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val images: Images? = null,
    val title: String? = "",
    val titleEnglish: String? = "",
    val titleJapanese: String? = "",
    val status: String? = "",
    val type: String? = "",
    val episodes: Int? = 0,
    val rating: String? = "",
    val synopsis: String? = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

data class AnimeEntityWrapper(
    @Embedded val animeEntity: AnimeEntity,
    val isFavourite: Boolean
)