package com.yugen.animeapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropResponse(
    @SerialName("from")
    val from: PropDetailResponse,
    @SerialName("to")
    val to: PropDetailResponse
)

@Serializable
data class PropDetailResponse(
    @SerialName("day")
    val day: Int?,
    @SerialName("month")
    val month: Int?,
    @SerialName("year")
    val year: Int?
)
