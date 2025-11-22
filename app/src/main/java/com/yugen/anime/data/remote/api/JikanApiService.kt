package com.yugen.anime.data.remote.api

import com.yugen.anime.data.remote.model.AnimeResponse
import com.yugen.anime.data.remote.model.AnimeDetailsResponse
import com.yugen.anime.data.remote.model.DataResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

    @GET("top/anime")
    suspend fun fetchTopAnime(): DataResponse<List<AnimeResponse>>

    @GET("anime")
    suspend fun fetchAnimeListByGenreId(
        @Query("genres") genreId: Int,
        @Query("order_by") orderBy: String = "popularity",
        @Query("limit") limit: Int = 20
    ): DataResponse<List<AnimeResponse>>

    @GET("anime/{animeId}")
    suspend fun getAnimeById(
        @Path("animeId") animeId: Int
    ): DataResponse<AnimeDetailsResponse>
}