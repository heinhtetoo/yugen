package com.yugen.animeapp.data.remote.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yugen.animeapp.data.mapper.toAnime
import com.yugen.animeapp.domain.model.Anime
import retrofit2.HttpException
import java.io.IOException

class AnimePagingSource(
    private val api: JikanApiService,
    private val animeGenreId: Int? = null,
    private val animeType: String? = null,
    private val animeStatus: String? = null,
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
                !searchQuery.isNullOrEmpty() -> api.searchAnime(
                    query = searchQuery,
                    type = animeType,
                    status = animeStatus,
                    page = page
                )

                animeGenreId != null -> api.fetchAnimeListByGenreId(
                    genreId = animeGenreId,
                    page = page
                )

                else -> throw IllegalArgumentException("No search query or anime genre id provided")
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