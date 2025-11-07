package com.yugen.anime.data.remote.api

import com.yugen.anime.data.remote.model.Anime
import com.yugen.anime.data.remote.model.AnimeDetails
import com.yugen.anime.data.remote.model.DataResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApiService {

    @GET("top/anime")
    suspend fun fetchTopAnime(): DataResponse<List<Anime>>

    @GET("anime/{animeId}")
    suspend fun getAnimeById(
        @Path("animeId") animeId: Int
    ): DataResponse<AnimeDetails>
}