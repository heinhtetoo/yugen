package com.yugen.animeapp.data.mapper

import com.yugen.animeapp.data.local.entity.AnimeGenreEntity
import com.yugen.animeapp.data.local.entity.ChatMessageEntity
import com.yugen.animeapp.data.local.model.AnimeItem
import com.yugen.animeapp.data.local.model.LibraryItem
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.AnimeDetails
import com.yugen.animeapp.domain.model.AnimeGenre
import com.yugen.animeapp.domain.model.ChatMessage

fun AnimeGenreEntity.toAnimeGenre(): AnimeGenre =
    AnimeGenre(
        id = id,
        type = type,
        name = name,
        count = count
    )

fun AnimeItem.toAnime(): Anime =
    Anime(
        id = animeEntity.id,
        images = animeEntity.images,
        title = animeEntity.title,
        status = animeEntity.status,
        synopsis = animeEntity.synopsis,
        isFavourite = isFavourite
    )

fun AnimeItem.toAnimeDetails(): AnimeDetails =
    AnimeDetails(
        id = animeEntity.id,
        images = animeEntity.images,
        title = animeEntity.title,
        titleEnglish = animeEntity.titleEnglish,
        titleJapanese = animeEntity.titleJapanese,
        type = animeEntity.type,
        episodes = animeEntity.episodes,
        status = animeEntity.status,
        airing = animeEntity.airing,
        airedFromYear = animeEntity.airedFromYear,
        airedToYear = animeEntity.airedToYear,
        rating = animeEntity.rating,
        score = animeEntity.score,
        scoredBy = animeEntity.scoredBy,
        rank = animeEntity.rank,
        favourites = animeEntity.favourites,
        synopsis = animeEntity.synopsis,
        isFavourite = isFavourite
    )

fun LibraryItem.toAnime(): Anime =
    Anime(
        id = animeEntity.id,
        images = animeEntity.images,
        title = animeEntity.title,
        status = animeEntity.status,
        synopsis = animeEntity.synopsis,
        isFavourite = isFavourite
    )

fun LibraryItem.toAnimeDetails(): AnimeDetails =
    AnimeDetails(
        id = animeEntity.id,
        images = animeEntity.images,
        title = animeEntity.title,
        titleEnglish = animeEntity.titleEnglish,
        titleJapanese = animeEntity.titleJapanese,
        type = animeEntity.type,
        episodes = animeEntity.episodes,
        status = animeEntity.status,
        airing = animeEntity.airing,
        airedFromYear = animeEntity.airedFromYear,
        airedToYear = animeEntity.airedToYear,
        rating = animeEntity.rating,
        score = animeEntity.score,
        scoredBy = animeEntity.scoredBy,
        rank = animeEntity.rank,
        favourites = animeEntity.favourites,
        synopsis = animeEntity.synopsis,
        isFavourite = isFavourite
    )

fun ChatMessageEntity.toChatMessage(): ChatMessage =
    ChatMessage(
        id = id,
        text = text,
        isUser = isUser,
        timestamp = timestamp,
        isError = isError
    )