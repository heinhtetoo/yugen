package com.yugen.animeapp.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yugen.animeapp.domain.model.Images

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val images: Images? = null,
    val title: String? = "",
    val titleEnglish: String? = "",
    val titleJapanese: String? = "",
    val status: String? = "",
    val airing: Boolean = false,
    val airedFromYear: Int? = null,
    val airedToYear: Int? = null,
    val type: String? = "",
    val episodes: Int? = 0,
    val rating: String? = "",
    val score: Double? = 0.0,
    val scoredBy: Int? = 0,
    val rank: Int? = 0,
    val favourites: Int? = 0,
    val synopsis: String? = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

data class AnimeEntityWrapper(
    @Embedded val animeEntity: AnimeEntity,
    val isFavourite: Boolean
)