package com.yugen.anime.domain.model

data class AnimeDetails(
    val id: Int,
    val images: Images?,
    val title: String?,
    val titleEnglish: String?,
    val titleJapanese: String?,
    val type: String?,
    val episodes: Int,
    val status: String?,
    val rating: String?,
    val synopsis: String?
)
