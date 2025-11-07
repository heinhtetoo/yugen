package com.yugen.anime.domain.repository

import com.yugen.anime.data.remote.model.Anime
import com.yugen.anime.data.remote.model.AnimeDetails
import com.yugen.anime.data.remote.model.DataResponse

interface JikanRepository {

    suspend fun fetchTopAnime(): DataResponse<List<Anime>>
    suspend fun getAnimeDetailsById(animeId: Int): DataResponse<AnimeDetails>
}