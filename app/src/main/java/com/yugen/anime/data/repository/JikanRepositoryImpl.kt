package com.yugen.anime.data.repository

import com.yugen.anime.data.remote.api.JikanApiService
import com.yugen.anime.data.remote.model.AnimeResponse
import com.yugen.anime.data.remote.model.AnimeDetailsResponse
import com.yugen.anime.data.remote.model.DataResponse
import com.yugen.anime.domain.repository.JikanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JikanRepositoryImpl @Inject constructor(
    private val jikanApiService: JikanApiService
) : JikanRepository {

    override suspend fun fetchTopAnime(): DataResponse<List<AnimeResponse>> =
        jikanApiService.fetchTopAnime()

    override suspend fun getAnimeDetailsById(animeId: Int): DataResponse<AnimeDetailsResponse> =
        jikanApiService.getAnimeById(animeId = animeId)
}