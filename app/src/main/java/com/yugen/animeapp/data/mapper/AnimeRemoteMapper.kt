package com.yugen.animeapp.data.mapper

import com.yugen.animeapp.data.local.entities.AnimeEntity
import com.yugen.animeapp.data.local.entities.AnimeGenreCrossRefEntity
import com.yugen.animeapp.data.local.entities.AnimeGenreEntity
import com.yugen.animeapp.data.remote.model.AnimeDetailsResponse
import com.yugen.animeapp.data.remote.model.AnimeGenreResponse
import com.yugen.animeapp.data.remote.model.AnimeResponse
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

fun AnimeResponse.toAnimeGenreCrossRefEntityList(): List<AnimeGenreCrossRefEntity> {
    return genres?.mapIndexed { index, genre ->
        AnimeGenreCrossRefEntity(
            genreId = genre.id,
            animeId = id,
            position = index
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
        rating = rating,
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
        rating = rating,
        synopsis = synopsis
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