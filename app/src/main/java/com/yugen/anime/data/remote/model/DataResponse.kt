package com.yugen.anime.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataResponse<T>(
    @SerialName("data")
    val data: T?,
    @SerialName("pagination")
    val pagination: Pagination? = null
)

@Serializable
data class Pagination(
    @SerialName("last_visible_page")
    val lastVisiblePage: Int,
    @SerialName("has_next_page")
    val hasNextPage: Boolean,
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("items")
    val items: Items,
)

@Serializable
data class Items(
    @SerialName("count")
    val count: Int,
    @SerialName("total")
    val total: Int,
    @SerialName("per_page")
    val perPage: Int
)
