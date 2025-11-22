package com.yugen.anime.ui.screen.favourite

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
import androidx.compose.material3.Text
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
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeSource

@Composable
fun FavouriteAnimeScreen(
    navigateToAnimeDetails: (Int, AnimeSource) -> Unit,
    modifier: Modifier = Modifier,
    favouriteAnimeViewModel: FavouriteAnimeViewModel = hiltViewModel()
) {

    val favouriteAnimeUiState by favouriteAnimeViewModel.uiState.collectAsState()

    FavouriteAnimeBody(
        favouriteAnimeUiState = favouriteAnimeUiState,
        onAnimeClick = navigateToAnimeDetails,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium))
    )
}

@Composable
fun FavouriteAnimeBody(
    favouriteAnimeUiState: FavouriteAnimeUiState,
    onAnimeClick: (Int, AnimeSource) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (favouriteAnimeUiState) {
            is FavouriteAnimeUiState.Idle -> Text(stringResource(R.string.idle))
            is FavouriteAnimeUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is FavouriteAnimeUiState.Error -> ErrorBody(favouriteAnimeUiState.message, favouriteAnimeUiState.details)
            is FavouriteAnimeUiState.Success -> FavouriteAnimeList(
                data = favouriteAnimeUiState.data,
                onAnimeClick = onAnimeClick,
                contentPadding = contentPadding,
                Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun FavouriteAnimeList(
    data: List<Anime>,
    onAnimeClick: (Int, AnimeSource) -> Unit,
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
                onAnimeClick = { onAnimeClick(anime.id, AnimeSource.FAVORITE) },
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
        details?.let { Text(it, maxLines = 3) }
    }
}

@Preview
@Composable
private fun AnimeListPreview() {
    FavouriteAnimeList(
        listOf(
            Anime(1, null, "Title 1", "Status 1", "Synopsis 1"),
            Anime(2, null, "Title 2", "Status 2", "Synopsis 2")
        ),
        { _, _ -> }, PaddingValues(32.dp)
    )
}