package com.yugen.anime.data.remote.api

import com.yugen.anime.data.remote.model.AnimeResponse
import com.yugen.anime.data.remote.model.AnimeDetailsResponse
import com.yugen.anime.data.remote.model.DataResponse
import com.yugen.anime.data.remote.model.AnimeGenreResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

    @GET("genres/anime")
    suspend fun fetchAnimeGenres(): DataResponse<List<AnimeGenreResponse>>

    @GET("top/anime")
    suspend fun fetchTopAnime(
        @Query("filter") filter: String,
        @Query("page") page: Int
    ): DataResponse<List<AnimeResponse>>

    @GET("anime")
    suspend fun fetchAnimeListByGenreId(
        @Query("genres") genreId: Int,
        @Query("order_by") orderBy: String = "popularity",
        @Query("sort") sort: String = "desc",
        @Query("page") page: Int
    ): DataResponse<List<AnimeResponse>>

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int
    ): DataResponse<List<AnimeResponse>>

    @GET("anime/{animeId}")
    suspend fun getAnimeById(
        @Path("animeId") animeId: Int
    ): DataResponse<AnimeDetailsResponse>
}