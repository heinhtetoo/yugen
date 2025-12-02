package com.yugen.anime.data.repository

import com.yugen.anime.data.local.dao.AnimeDao
import com.yugen.anime.data.local.entities.FavouriteAnimeEntity
import com.yugen.anime.data.mapper.toAnime
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.repository.FavouriteAnimeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
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