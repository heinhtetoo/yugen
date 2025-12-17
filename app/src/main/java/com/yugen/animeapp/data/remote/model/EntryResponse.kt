package com.yugen.animeapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntryResponse(
    @SerialName("entry")
    val entry: EntryDetailsResponse
)

@Serializable
data class EntryDetailsResponse(
    @SerialName("mal_id")
    val id: Int,
    @SerialName("url")
    val url: String,
    @SerialName("images")
    val images: ImagesResponse,
    @SerialName("title")
    val title: String
)
