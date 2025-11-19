package com.yugen.anime.data.remote.api

import com.yugen.anime.data.remote.model.AnimeResponse
import com.yugen.anime.data.remote.model.AnimeDetailsResponse
import com.yugen.anime.data.remote.model.DataResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApiService {

    @GET("top/anime")
    suspend fun fetchTopAnime(): DataResponse<List<AnimeResponse>>

    @GET("anime/{animeId}")
    suspend fun getAnimeById(
        @Path("animeId") animeId: Int
    ): DataResponse<AnimeDetailsResponse>
}