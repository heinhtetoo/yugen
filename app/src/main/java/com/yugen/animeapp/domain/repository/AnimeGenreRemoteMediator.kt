package com.yugen.animeapp.domain.repository

import androidx.paging.PagingState
import com.yugen.animeapp.data.local.entity.AnimeEntity
import com.yugen.animeapp.data.local.entity.AnimeRemoteKeys

interface AnimeGenreRemoteMediator {

    suspend fun getRemoteKeyForLastItem(state: PagingState<Int, AnimeEntity>): AnimeRemoteKeys?

    suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, AnimeEntity>): AnimeRemoteKeys?

    suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, AnimeEntity>): AnimeRemoteKeys?
}