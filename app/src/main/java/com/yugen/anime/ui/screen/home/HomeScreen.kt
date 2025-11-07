package com.yugen.anime.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yugen.anime.R
import com.yugen.anime.data.remote.model.Anime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAnimeDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val topAnimeUiState by homeViewModel.uiState.collectAsState()

    HomeBody(
        homeUiState = topAnimeUiState,
        onAnimeClick = navigateToAnimeDetails,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium))
    )
}

@Composable
fun HomeBody(
    homeUiState: HomeUiState,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (homeUiState) {
            is HomeUiState.Idle -> Text(stringResource(R.string.idle))
            is HomeUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is HomeUiState.Error -> ErrorBody(homeUiState.message, homeUiState.details)
            is HomeUiState.Success -> TopAnimeList(
                data = homeUiState.data,
                onAnimeClick = onAnimeClick,
                contentPadding = contentPadding,
                Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun TopAnimeList(
    data: List<Anime>,
    onAnimeClick: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(data) { anime ->
            AnimeItem(
                anime = anime,
                onAnimeClick = { onAnimeClick(anime.id) },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun AnimeItem(
    anime: Anime,
    onAnimeClick: (Anime) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
            .clickable {
                onAnimeClick(anime)
            },
        elevation = CardDefaults.cardElevation()
    ) {
        Row(Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Column {
                Text(anime.title ?: "Unknown", fontWeight = FontWeight.Bold)
                anime.status?.let { Text(it) }
                anime.synopsis?.let { Text(it, maxLines = 5) }
            }
        }
    }
}

@Composable
fun ErrorBody(
    message: String,
    details: String?,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, fontWeight = FontWeight.Bold)
        details?.let { Text(it, maxLines = 2) }
    }
}

@Preview
@Composable
private fun AnimeListPreview() {
    TopAnimeList(
        listOf(
            Anime(1, "Title 1", "Status 1", "Synopsis 1"),
            Anime(2, "Title 2", "Status 2", "Synopsis 2")
        ),
        {}, PaddingValues(32.dp)
    )
}