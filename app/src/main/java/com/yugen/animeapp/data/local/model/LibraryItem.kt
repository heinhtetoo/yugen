package com.yugen.animeapp.data.local.model

import androidx.room.Embedded
import com.yugen.animeapp.data.local.entities.AnimeEntity
import com.yugen.animeapp.domain.model.WatchStatus

data class LibraryItem(
    @Embedded val animeEntity: AnimeEntity,
    val watchStatus: WatchStatus?,
    val isFavourite: Boolean
)