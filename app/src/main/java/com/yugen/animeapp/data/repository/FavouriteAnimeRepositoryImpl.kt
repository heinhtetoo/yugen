package com.yugen.animeapp.data.repository

import com.yugen.animeapp.data.local.dao.AnimeDao
import com.yugen.animeapp.data.local.entities.FavouriteAnimeEntity
import com.yugen.animeapp.data.mapper.toAnime
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.repository.FavouriteAnimeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteAnimeRepositoryImpl @Inject constructor(
    private val animeDao: AnimeDao
) : FavouriteAnimeRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavouriteAnime(): Flow<List<Anime>> =
        animeDao.getFavouriteAnime().map { it.map { anime -> anime.toAnime() } }

    override suspend fun addFavouriteAnime(animeId: Int) {
        animeDao.insertFavouriteAnime(FavouriteAnimeEntity(animeId = animeId))
    }

    override suspend fun removeFavouriteAnime(animeId: Int) {
        animeDao.deleteFavouriteAnime(animeId = animeId)
    }

    override fun isFavouriteAnime(animeId: Int): Flow<Boolean> =
        animeDao.isFavouriteAnime(animeId)
}