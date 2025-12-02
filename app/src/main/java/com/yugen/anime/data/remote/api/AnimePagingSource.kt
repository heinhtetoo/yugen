package com.yugen.anime.data.remote.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yugen.anime.data.mapper.toAnime
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeGenre
import retrofit2.HttpException
import java.io.IOException

class AnimePagingSource(
    private val api: JikanApiService,
    private val animeGenre: AnimeGenre? = null,
    private val searchQuery: String? = null
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
            val response = when {
                !searchQuery.isNullOrEmpty() -> api.searchAnime(query = searchQuery, page = page)
                animeGenre != null -> {
                    when (animeGenre) {
                        AnimeGenre.TOP_AIRING -> api.fetchTopAnime(filter = "airing", page = page)
                        AnimeGenre.TOP_UPCOMING -> api.fetchTopAnime(filter = "upcoming", page = page)
                        AnimeGenre.AWARD_WINNING -> api.fetchAnimeListByGenreId(genreId = 42, page = page)
                        AnimeGenre.FANTASY -> api.fetchAnimeListByGenreId(genreId = 10, page = page)
                        else -> api.searchAnime(query = "", page = page)
                    }
                }
                else -> throw IllegalArgumentException("No query or category provided")
            }

            LoadResult.Page(
                data = response.data?.map { it.toAnime() } ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.pagination?.hasNextPage ?: false) page + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}