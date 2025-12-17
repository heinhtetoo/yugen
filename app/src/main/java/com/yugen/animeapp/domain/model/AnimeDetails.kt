package com.yugen.animeapp.domain.model

data class AnimeDetails(
    val id: Int,
    val images: Images?,
    val title: String?,
    val titleEnglish: String?,
    val titleJapanese: String?,
    val type: String?,
    val episodes: Int?,
    val status: String?,
    val airing: Boolean,
    val airedFromYear: Int?,
    val airedToYear: Int?,
    val rating: String?,
    val score: Double?,
    val scoredBy: Int?,
    val rank: Int?,
    val favourites: Int?,
    val synopsis: String?,
    val isFavourite: Boolean? = null
)
