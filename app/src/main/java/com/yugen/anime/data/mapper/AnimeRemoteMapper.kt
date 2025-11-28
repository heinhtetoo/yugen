package com.yugen.anime.data.mapper

import com.yugen.anime.data.local.entities.AnimeEntity
import com.yugen.anime.data.remote.model.AnimeDetailsResponse
import com.yugen.anime.data.remote.model.AnimeResponse
import com.yugen.anime.data.remote.model.ImageResponse
import com.yugen.anime.data.remote.model.ImagesResponse
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import com.yugen.anime.domain.model.Image
import com.yugen.anime.domain.model.Images

fun AnimeResponse.toAnime(): Anime =
    Anime(
        id = id,
        images = images.toImages(),
        title = title,
        status = status,
        synopsis = synopsis
    )

fun AnimeResponse.toAnimeEntity(
    isFavourite: Boolean = false,
    isTopAiring: Boolean = false,
    isTopUpcoming: Boolean = false,
    isAwardWinning: Boolean = false,
    isFantasy: Boolean = false
): AnimeEntity =
    AnimeEntity(
        id = id,
        images = images.toImages(),
        title = title,
        status = status,
        synopsis = synopsis,
        isFavourite = isFavourite,
        isTopAiring = isTopAiring,
        isTopUpcoming = isTopUpcoming,
        isAwardWinning = isAwardWinning,
        isFantasy = isFantasy
    )

fun AnimeDetailsResponse.toAnimeDetails(): AnimeDetails =
    AnimeDetails(
        id = id,
        images = images.toImages(),
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type,
        episodes = episodes,
        status = status,
        rating = rating,
        synopsis = synopsis
    )

fun AnimeDetailsResponse.toAnimeEntity(
    isFavourite: Boolean = false,
    isTopAiring: Boolean = false,
    isTopUpcoming: Boolean = false,
    isAwardWinning: Boolean = false,
    isFantasy: Boolean = false
): AnimeEntity =
    AnimeEntity(
        id = id,
        images = images.toImages(),
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type,
        episodes = episodes,
        status = status,
        rating = rating,
        synopsis = synopsis,
        isFavourite = isFavourite,
        isTopAiring = isTopAiring,
        isTopUpcoming = isTopUpcoming,
        isAwardWinning = isAwardWinning,
        isFantasy = isFantasy
    )

fun ImagesResponse.toImages(): Images =
    Images(
        jpg = jpg.toImage(),
        webp = webp.toImage()
    )

fun ImageResponse.toImage(): Image =
    Image(
        imageUrl = imageUrl,
        smallImageUrl = smallImageUrl,
        largeImageUrl = largeImageUrl
    )