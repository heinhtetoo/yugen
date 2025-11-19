package com.yugen.anime.data.repository

import com.yugen.anime.data.local.dao.AnimeDao
import com.yugen.anime.data.local.dao.FavouriteAnimeDao
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
    private val animeDao: AnimeDao,
    private val favouriteDao: FavouriteAnimeDao
) : FavouriteAnimeRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavouriteAnime(): Flow<List<Anime>> =
        favouriteDao.getFavouriteAnimeIds().flatMapLatest { ids ->
            animeDao.getAnimeListByIds(ids).map { list ->
                list.map { it.toAnime() }
            }
        }

    override fun isFavourite(id: Int): Flow<Boolean> =
        favouriteDao.isFavourite(id)

    override suspend fun addFavouriteAnime(id: Int) {
        favouriteDao.insertFavouriteAnime(FavouriteAnimeEntity(id = id))
    }

    override suspend fun removeFavouriteAnime(id: Int) {
        favouriteDao.deleteFavouriteAnime(id = id)
    }
}