package com.yugen.animeapp.data.mapper

import com.yugen.animeapp.data.local.entities.AnimeEntityWrapper
import com.yugen.animeapp.data.local.entities.AnimeGenreEntity
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.AnimeDetails
import com.yugen.animeapp.domain.model.AnimeGenre

fun AnimeGenreEntity.toAnimeGenre(): AnimeGenre =
    AnimeGenre(
        id = id,
        type = type,
        name = name,
        count = count
    )

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