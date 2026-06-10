package com.yugen.animeapp.data.mapper

import com.yugen.animeapp.data.local.entity.AnimeEntity
import com.yugen.animeapp.data.local.entity.AnimeGenreCrossRefEntity
import com.yugen.animeapp.data.local.entity.AnimeGenreEntity
import com.yugen.animeapp.data.remote.model.AnimeDetailsResponse
import com.yugen.animeapp.data.remote.model.AnimeGenreResponse
import com.yugen.animeapp.data.remote.model.AnimeResponse
import com.yugen.animeapp.data.remote.model.EntryResponse
import com.yugen.animeapp.data.remote.model.ImageResponse
import com.yugen.animeapp.data.remote.model.ImagesResponse
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.AnimeDetails
import com.yugen.animeapp.domain.model.AnimeGenre
import com.yugen.animeapp.domain.model.Image
import com.yugen.animeapp.domain.model.Images

fun AnimeGenreResponse.toAnimeGenre(): AnimeGenre =
    AnimeGenre(
        id = id,
        type = type,
        name = name,
        count = count
    )

fun AnimeGenreResponse.toAnimeGenreEntity(): AnimeGenreEntity =
    AnimeGenreEntity(
        id = id,
        type = type,
        name = name,
        count = count
    )

fun AnimeResponse.toAnime(): Anime =
    Anime(
        id = id,
        images = images.toImages(),
        title = title,
        status = status,
        synopsis = synopsis
    )

fun AnimeResponse.toAnimeEntity(): AnimeEntity =
    AnimeEntity(
        id = id,
        images = images.toImages(),
        title = title,
        status = status,
        synopsis = synopsis
    )

fun AnimeResponse.toAnimeGenreCrossRefEntityList(position: Int): List<AnimeGenreCrossRefEntity> {
    return genres?.map { genre ->
        AnimeGenreCrossRefEntity(
            genreId = genre.id,
            animeId = id,
            position = position
        )
    } ?: emptyList()
}

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
        airing = airing,
        airedFromYear = aired.prop.from.year,
        airedToYear = aired.prop.to.year,
        rating = rating,
        score = score,
        scoredBy = scoredBy,
        rank = rank,
        favourites = favourites,
        synopsis = synopsis
    )

fun AnimeDetailsResponse.toAnimeEntity(): AnimeEntity =
    AnimeEntity(
        id = id,
        images = images.toImages(),
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type,
        episodes = episodes,
        status = status,
        airing = airing,
        airedFromYear = aired.prop.from.year,
        airedToYear = aired.prop.to.year,
        rating = rating,
        score = score,
        scoredBy = scoredBy,
        rank = rank,
        favourites = favourites,
        synopsis = synopsis
    )

fun EntryResponse.toAnime(): Anime =
    Anime(
        id = entry.id,
        images = entry.images.toImages(),
        title = entry.title,
        status = "",
        synopsis = ""
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