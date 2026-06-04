package com.yugen.animeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_remote_keys")
data class AnimeRemoteKeys(
    @PrimaryKey val animeId: Int,
    val genreId: Int,
    val prevPage: Int?,
    val nextPage: Int?
)
