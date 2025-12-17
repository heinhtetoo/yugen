package com.yugen.animeapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailsResponse(
    @SerialName("mal_id")
    val id: Int,
    @SerialName("images")
    val images: ImagesResponse,
    @SerialName("title")
    val title: String?,
    @SerialName("title_english")
    val titleEnglish: String?,
    @SerialName("title_japanese")
    val titleJapanese: String?,
    @SerialName("type")
    val type: String?,
    @SerialName("episodes")
    val episodes: Int?,
    @SerialName("status")
    val status: String?,
    @SerialName("airing")
    val airing: Boolean,
    @SerialName("aired")
    val aired: AiredResponse,
    @SerialName("rating")
    val rating: String?,
    @SerialName("score")
    val score: Double?,
    @SerialName("scored_by")
    val scoredBy: Int?,
    @SerialName("rank")
    val rank: Int?,
    @SerialName("favorites")
    val favourites: Int?,
    @SerialName("synopsis")
    val synopsis: String?,
    @SerialName("genres")
    val genres: List<AnimeGenreResponse>?
)
