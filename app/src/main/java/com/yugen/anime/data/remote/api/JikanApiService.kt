package com.yugen.anime.data.remote.api

import com.yugen.anime.data.remote.model.DataResponse
import retrofit2.http.GET

interface JikanApiService {

    @GET("top/anime")
    suspend fun fetchTopAnime(): DataResponse
}