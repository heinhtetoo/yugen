package com.yugen.animeapp.data.local.model

import androidx.room.Embedded
import com.yugen.animeapp.data.local.entities.AnimeEntity

data class AnimeItem(
    @Embedded val animeEntity: AnimeEntity,
    val isFavourite: Boolean
)