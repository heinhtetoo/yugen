package com.yugen.anime.domain.model

import kotlinx.serialization.SerialName

data class Anime(
    val id: Int,
    val title: String?,
    val status: String?,
    val synopsis: String?
)
