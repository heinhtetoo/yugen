package com.yugen.anime.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    @SerialName("jpg")
    val jpg: ImageResponse,
    @SerialName("webp")
    val webp: ImageResponse
)
