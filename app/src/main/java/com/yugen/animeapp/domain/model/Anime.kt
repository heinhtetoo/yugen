package com.yugen.animeapp.domain.model

data class Anime(
    val id: Int,
    val images: Images?,
    val title: String?,
    val status: String?,
    val synopsis: String?,
    val isFavourite: Boolean? = null
)
