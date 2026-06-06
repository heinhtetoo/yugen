package com.yugen.animeapp.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "anime_remote_keys",
    primaryKeys = ["animeId", "genreId"]
)
data class AnimeRemoteKeys(
    val animeId: Int,
    val genreId: Int,
    val prevPage: Int?,
    val nextPage: Int?
)
