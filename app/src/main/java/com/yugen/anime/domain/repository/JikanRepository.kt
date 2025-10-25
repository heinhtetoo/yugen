package com.yugen.anime.domain.repository

import com.yugen.anime.data.remote.model.DataResponse

interface JikanRepository {

    suspend fun fetchTopAnime(): DataResponse
}