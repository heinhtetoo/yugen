package com.yugen.anime.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeGenreResponse(
    @SerialName("mal_id")
    val id: Int,
    @SerialName("type")
    val type: String? = null,
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
    @SerialName("count")
    val count: Int? = null
)