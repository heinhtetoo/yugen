package com.yugen.anime.data.mapper

import com.yugen.anime.data.local.entities.AnimeEntity
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails

fun AnimeEntity.toAnime(): Anime =
    Anime(
        id = id,
        images = images,
        title = title,
        status = status,
        synopsis = synopsis
    )

fun AnimeEntity.toAnimeDetails(): AnimeDetails =
    AnimeDetails(
        id = id,
        images = images,
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type,
        episodes = episodes,
        status = status,
        rating = rating,
        synopsis = synopsis
    )