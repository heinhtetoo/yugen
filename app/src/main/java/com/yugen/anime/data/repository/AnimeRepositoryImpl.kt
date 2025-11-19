package com.yugen.anime.data.repository

import com.yugen.anime.data.local.dao.AnimeDao
import com.yugen.anime.data.mapper.toAnime
import com.yugen.anime.data.mapper.toAnimeDetails
import com.yugen.anime.data.mapper.toAnimeEntity
import com.yugen.anime.data.remote.api.JikanApiService
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeDetails
import com.yugen.anime.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApiService,
    private val dao: AnimeDao
) : AnimeRepository {

    override fun getTopAnime(): Flow<List<Anime>> =
        dao.getTopAnime()
            .map { list -> list.map { it.toAnime() } }

    override fun getAnimeDetailsById(id: Int): Flow<AnimeDetails?> =
        dao.getAnimeDetailsById(id)
            .map { entity -> entity?.toAnimeDetails() }

    override suspend fun refreshTopAnime() {
        val response = api.fetchTopAnime()
        val list = response.data ?: emptyList()
        dao.insertAnimeList(list.map { it.toAnimeEntity(isTop = true) })
    }

    override suspend fun fetchAnimeDetailsById(id: Int, isTop: Boolean, isFavourite: Boolean) {
        val response = api.getAnimeById(id)
        val details = response.data

        details?.let {
            dao.insertAnime(it.toAnimeEntity(isTop, isFavourite))
        }
    }
}