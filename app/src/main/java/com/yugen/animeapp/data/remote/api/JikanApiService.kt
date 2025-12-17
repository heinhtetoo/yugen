package com.yugen.animeapp.data.remote.api

import com.yugen.animeapp.data.remote.model.AnimeResponse
import com.yugen.animeapp.data.remote.model.AnimeDetailsResponse
import com.yugen.animeapp.data.remote.model.DataResponse
import com.yugen.animeapp.data.remote.model.AnimeGenreResponse
import com.yugen.animeapp.data.remote.model.EntryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

    @GET("genres/anime")
    suspend fun fetchAnimeGenres(
        @Query("filter") filter: String = "genres"
    ): DataResponse<List<AnimeGenreResponse>>

    @GET("top/anime")
    suspend fun fetchTopAnimeByFilterEnum(
        @Query("filter") filter: String,
        @Query("page") page: Int
    ): DataResponse<List<AnimeResponse>>

    @GET("anime")
    suspend fun fetchAnimeListByGenreId(
        @Query("genres") genreId: Int,
        @Query("order_by") orderBy: String = "popularity",
        @Query("sort") sort: String = "asc",
        @Query("page") page: Int
    ): DataResponse<List<AnimeResponse>>

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int
    ): DataResponse<List<AnimeResponse>>

    @GET("anime/{animeId}")
    suspend fun fetchAnimeDetailsById(
        @Path("animeId") animeId: Int
    ): DataResponse<AnimeDetailsResponse>

    @GET("anime/{animeId}/recommendations")
    suspend fun fetchAnimeRecommendationsById(
        @Path("animeId") animeId: Int
    ): DataResponse<List<EntryResponse>>
}