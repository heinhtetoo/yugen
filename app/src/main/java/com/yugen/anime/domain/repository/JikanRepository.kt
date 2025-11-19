package com.yugen.anime.domain.repository

import com.yugen.anime.data.remote.model.AnimeResponse
import com.yugen.anime.data.remote.model.AnimeDetailsResponse
import com.yugen.anime.data.remote.model.DataResponse

interface JikanRepository {

    suspend fun fetchTopAnime(): DataResponse<List<AnimeResponse>>
    suspend fun getAnimeDetailsById(animeId: Int): DataResponse<AnimeDetailsResponse>
}