package com.yugen.anime.data.mapper

import com.yugen.anime.data.local.entities.AnimeEntity
import com.yugen.anime.data.remote.model.AnimeDetailsResponse
import com.yugen.anime.data.remote.model.AnimeResponse
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import kotlin.Int

fun AnimeResponse.toAnime(): Anime {
    return Anime(
        id = id,
        title = title,
        status = status,
        synopsis = synopsis
    )
}

fun AnimeResponse.toAnimeEntity(isTop: Boolean = false, isFavourite: Boolean = false): AnimeEntity {
    return AnimeEntity(
        id = id,
        title = title,
        status = status,
        synopsis = synopsis,
        isTop = isTop,
        isFavourite = isFavourite
    )
}

fun AnimeDetailsResponse.toAnimeDetails(): AnimeDetails {
    return AnimeDetails(
        id = id,
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type,
        episodes = episodes,
        status = status,
        rating = rating,
        synopsis = synopsis
    )
}

fun AnimeDetailsResponse.toAnimeEntity(isTop: Boolean = false, isFavourite: Boolean = false): AnimeEntity {
    return AnimeEntity(
        id = id,
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type,
        episodes = episodes,
        status = status,
        rating = rating,
        synopsis = synopsis,
        isTop = isTop,
        isFavourite = isFavourite
    )
}