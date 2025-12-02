package com.yugen.anime.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerialName("mal_id")
    val genreId: Int,
    @SerialName("type")
    val type: String,
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)