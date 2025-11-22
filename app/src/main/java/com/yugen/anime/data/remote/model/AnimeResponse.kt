package com.yugen.anime.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeResponse(
    @SerialName("mal_id")
    val id: Int,
    @SerialName("images")
    val images: ImagesResponse,
    @SerialName("title")
    val title: String?,
    @SerialName("status")
    val status: String?,
    @SerialName("synopsis")
    val synopsis: String?
)
