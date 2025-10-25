package com.yugen.anime.data.repository

import com.yugen.anime.data.remote.api.JikanApiService
import com.yugen.anime.data.remote.model.DataResponse
import com.yugen.anime.domain.repository.JikanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JikanRepositoryImpl @Inject constructor(
    private val jikanApiService: JikanApiService
) : JikanRepository {

    override suspend fun fetchTopAnime(): DataResponse =
        jikanApiService.fetchTopAnime()
}