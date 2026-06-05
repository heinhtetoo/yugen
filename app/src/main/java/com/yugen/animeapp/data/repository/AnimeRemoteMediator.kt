package com.yugen.animeapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.yugen.animeapp.data.local.YugenDatabase
import com.yugen.animeapp.data.local.entity.AnimeGenreCrossRefEntity
import com.yugen.animeapp.data.local.entity.AnimeRemoteKeys
import com.yugen.animeapp.data.local.model.AnimeItem
import com.yugen.animeapp.data.mapper.toAnimeEntity
import com.yugen.animeapp.data.remote.api.JikanApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class AnimeGenreRemoteMediator @AssistedInject constructor(
    private val api: JikanApiService,
    private val db: YugenDatabase,
    @Assisted private val genreId: Int
) : RemoteMediator<Int, AnimeItem>() {

    @AssistedFactory
    interface Factory {
        fun create(genreId: Int): AnimeGenreRemoteMediator
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AnimeItem>
    ): MediatorResult {
        return try {
            // Determine which page to fetch from the network
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1 // Jikan API starts at page 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextPage
                }
            }

            // Fetch data from the network
            val response = api.fetchAnimeListByGenreId(genreId = genreId, page = page)
            val animeList = response.data ?: emptyList()
            val endOfPaginationReached = response.pagination.let { it == null || !it.hasNextPage }

            db.withTransaction {
                // If it's a new refresh, clear old cached data for this genre
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys(genreId)
                    db.animeDao().deleteGenreLinksByGenreId(genreId)
                }

                // Calculate starting position
                val startingPosition = if (loadType == LoadType.REFRESH) {
                    0
                } else {
                    val currentMax = db.animeDao().getMaxPositionForGenre(genreId) ?: -1
                    currentMax + 1
                }

                // Calculate next/prev keys for Paging
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = animeList.map { anime ->
                    AnimeRemoteKeys(
                        animeId = anime.id,
                        genreId = genreId,
                        prevPage = prevKey,
                        nextPage = nextKey
                    )
                }

                // Map Network DTOs to Room Entities
                val entities = animeList.map { it.toAnimeEntity() }

                // Map CrossRefs WITH the position using mapIndexed to ensure correct ordering in the database
                val crossRefs = animeList.mapIndexed { index, anime ->
                    AnimeGenreCrossRefEntity(
                        animeId = anime.id,
                        genreId = genreId,
                        position = startingPosition + index)
                }

                // Insert new data and keys into the database
                db.remoteKeysDao().insertAll(keys)
                db.animeDao().refreshAnimeListWithGenreLinks(entities, crossRefs)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e) // Network issue (allows offline mode to still work!)
        } catch (e: HttpException) {
            MediatorResult.Error(e) // API issue (e.g., 404, 429)
        }
    }

    suspend fun getRemoteKeyForLastItem(state: PagingState<Int, AnimeItem>): AnimeRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { anime -> db.remoteKeysDao().remoteKeysAnimeId(animeId = anime.animeEntity.id, genreId) }
    }

    suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, AnimeItem>): AnimeRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { anime -> db.remoteKeysDao().remoteKeysAnimeId(anime.animeEntity.id, genreId) }
    }

    suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, AnimeItem>): AnimeRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.animeEntity?.id?.let { animeId ->
                db.remoteKeysDao().remoteKeysAnimeId(animeId, genreId)
            }
        }
    }
}