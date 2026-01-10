package com.yugen.animeapp.ui.screen.animelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.yugen.animeapp.R
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.ui.screen.home.AnimeItem

@Composable
fun AnimeListScreen(
    navigateToAnimeDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    animeListViewModel: AnimeListViewModel = hiltViewModel()
) {
    val lazyAnimeList: LazyPagingItems<Anime> =
        animeListViewModel.pagedAnime.collectAsLazyPagingItems()

    val isRefreshing =
        lazyAnimeList.loadState.refresh is LoadState.Loading && lazyAnimeList.itemCount > 0

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { lazyAnimeList.refresh() },
        modifier = modifier.fillMaxSize()
    ) {
        AnimeListBody(
            lazyData = lazyAnimeList,
            onAnimeClick = { navigateToAnimeDetails(it) },
            modifier = modifier
        )
    }
}

@Composable
fun AnimeListBody(
    lazyData: LazyPagingItems<Anime>,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                count = lazyData.itemCount,
                key = { index ->
                    "${lazyData[index]?.id ?: "placeholder"}-{$index}"
                }
            ) { index ->
                lazyData[index]?.let {
                    AnimeItem(
                        anime = it,
                        onAnimeClick = { onAnimeClick(it.id) },
                        modifier = Modifier
                            .aspectRatio(
                                dimensionResource(R.dimen.anime_width_normal).value
                                        / dimensionResource(R.dimen.anime_height_normal).value
                            )
                    )
                }
            }

            lazyData.apply {
                loadState.append.let { loadState ->
                    when (loadState) {
                        is LoadState.Loading -> {
                            item { LoadingFooter(modifier = Modifier.fillMaxWidth()) }
                        }

                        is LoadState.Error -> {
                            item {
                                ErrorFooter(
                                    error = loadState.error.localizedMessage ?: "Unknown Error",
                                    onRetry = { retry() }
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        if (lazyData.loadState.refresh is LoadState.Error) {
            Column(modifier = Modifier.fillMaxSize()) {
                Button(onClick = { lazyData.retry() }) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

@Composable
fun LoadingFooter(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorFooter(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Loading failed: $error", color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}