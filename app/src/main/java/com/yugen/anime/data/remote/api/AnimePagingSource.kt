package com.yugen.anime.data.remote.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yugen.anime.data.mapper.toAnime
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeSource
import retrofit2.HttpException
import java.io.IOException

class AnimePagingSource(
    private val api: JikanApiService,
    private val animeSource: AnimeSource
) : PagingSource<Int, Anime>() {

    override fun getRefreshKey(state: PagingState<Int, Anime>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        val page = params.key ?: 1

        return try {
            val response = when (animeSource) {
                AnimeSource.TOP_AIRING -> api.fetchTopAnime(filter = "airing", page = page)
                AnimeSource.TOP_UPCOMING -> api.fetchTopAnime(filter = "upcoming", page = page)
                AnimeSource.AWARD_WINNING -> api.fetchAnimeListByGenreId(genreId = 42, page = page)
                AnimeSource.FANTASY -> api.fetchAnimeListByGenreId(genreId = 10, page = page)
                else -> api.fetchTopAnime(filter = "airing", page = page)
            }

            val animeList = response.data?.map { it.toAnime() } ?: emptyList()

            val hasNextPage = response.pagination?.hasNextPage ?: false

            LoadResult.Page(
                data = animeList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (hasNextPage) page + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}