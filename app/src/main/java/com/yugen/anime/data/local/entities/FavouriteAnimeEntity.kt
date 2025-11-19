package com.yugen.anime.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_anime")
data class FavouriteAnimeEntity(
    @PrimaryKey
    val id: Int,
    val createdAt: Long = System.currentTimeMillis()
)
