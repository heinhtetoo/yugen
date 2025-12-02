package com.yugen.anime.data.mapper

import com.yugen.anime.data.local.entities.AnimeEntityWrapper
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails

fun AnimeEntityWrapper.toAnime(): Anime =
    Anime(
        id = animeEntity.id,
        images = animeEntity.images,
        title = animeEntity.title,
        status = animeEntity.status,
        synopsis = animeEntity.synopsis,
        isFavourite = isFavourite
    )

fun AnimeEntityWrapper.toAnimeDetails(): AnimeDetails =
    AnimeDetails(
        id = animeEntity.id,
        images = animeEntity.images,
        title = animeEntity.title,
        titleEnglish = animeEntity.titleEnglish,
        titleJapanese = animeEntity.titleJapanese,
        type = animeEntity.type,
        episodes = animeEntity.episodes,
        status = animeEntity.status,
        rating = animeEntity.rating,
        synopsis = animeEntity.synopsis,
        isFavourite = isFavourite
    )