package com.yugen.animeapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AiredResponse(
    @SerialName("from")
    val from: String?,
    @SerialName("to")
    val to: String?,
    @SerialName("prop")
    val prop: PropResponse
)
