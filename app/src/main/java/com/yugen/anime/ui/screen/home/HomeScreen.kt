package com.yugen.anime.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yugen.anime.R
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeGenre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSearch: () -> Unit,
    navigateToAnimeList: (AnimeGenre) -> Unit,
    navigateToAnimeDetails: (Int, AnimeGenre) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by homeViewModel.uiState.collectAsState()

    HomeBody(
        homeUiState = homeUiState,
        onSearchClick = navigateToSearch,
        onSeeMoreClick = navigateToAnimeList,
        onAnimeClick = navigateToAnimeDetails,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium))
    )
}

@Composable
fun HomeBody(
    homeUiState: HomeUiState,
    onSearchClick: () -> Unit,
    onSeeMoreClick: (AnimeGenre) -> Unit,
    onAnimeClick: (Int, AnimeGenre) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = dimensionResource(R.dimen.padding_small)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_2xlarge)),
                tint = Color.Unspecified
            )
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.search_anime),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_large))
                    .clickable(onClick = onSearchClick)
            )
        }
        AnimeSection(
            R.string.top_upcoming_anime,
            homeUiState.topUpcomingAnime,
            onSeeMoreClick,
            onAnimeClick,
            contentPadding = contentPadding
        )
        AnimeSection(
            R.string.top_airing_anime,
            homeUiState.topAiringAnime,
            onSeeMoreClick,
            onAnimeClick,
            contentPadding = contentPadding
        )
        AnimeSection(
            R.string.award_winning_anime,
            homeUiState.awardWinningAnime,
            onSeeMoreClick,
            onAnimeClick,
            contentPadding = contentPadding
        )
        AnimeSection(
            R.string.fantasy_anime,
            homeUiState.fantasyAnime,
            onSeeMoreClick,
            onAnimeClick,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun AnimeSection(
    titleResId: Int,
    listUiState: ListUiState<Anime>,
    onSeeMoreClick: (AnimeGenre) -> Unit,
    onAnimeClick: (Int, AnimeGenre) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(titleResId),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                onClick = { onSeeMoreClick(getAnimeCategory(titleResId)) }) {
                Text(stringResource(R.string.see_more))
            }
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
        when (listUiState) {
            is ListUiState.Idle -> Text(stringResource(R.string.idle))
            is ListUiState.Loading -> Box(
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(getAnimeHeightDimenResId(titleResId))),
                Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is ListUiState.Error -> ErrorBody(
                listUiState.message, listUiState.details,
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(getAnimeHeightDimenResId(titleResId)))
            )

            is ListUiState.Success -> AnimeList(
                titleResId = titleResId,
                data = listUiState.data,
                onAnimeClick = onAnimeClick,
                contentPadding = contentPadding,
                Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
    }
}

@Composable
fun AnimeList(
    titleResId: Int,
    data: List<Anime>,
    onAnimeClick: (Int, AnimeGenre) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {

    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(data) { anime ->
            AnimeItem(
                anime = anime,
                onAnimeClick = { onAnimeClick(anime.id, getAnimeCategory(titleResId)) },
                modifier = Modifier
                    .size(
                        dimensionResource(getAnimeWidthDimenResId(titleResId)),
                        dimensionResource(getAnimeHeightDimenResId(titleResId))
                    )
                    .padding(dimensionResource(R.dimen.padding_small))
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
        modifier = modifier.clickable { onAnimeClick(anime) },
        elevation = CardDefaults.cardElevation()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(anime.images?.jpg?.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.round_image_24),
            error = painterResource(R.drawable.round_broken_image_24),
            contentDescription = anime.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, fontWeight = FontWeight.Bold)
        details?.let { Text(it, maxLines = 3) }
    }
}

private fun getAnimeWidthDimenResId(titleResId: Int) =
    if (titleResId == R.string.top_airing_anime) R.dimen.anime_width_large
    else R.dimen.anime_width_normal

private fun getAnimeHeightDimenResId(titleResId: Int) =
    if (titleResId == R.string.top_airing_anime) R.dimen.anime_height_large
    else R.dimen.anime_height_normal

private fun getAnimeCategory(titleResId: Int) =
    when (titleResId) {
        R.string.top_airing_anime -> AnimeGenre.TOP_AIRING
        R.string.top_upcoming_anime -> AnimeGenre.TOP_UPCOMING
        R.string.award_winning_anime -> AnimeGenre.AWARD_WINNING
        R.string.fantasy_anime -> AnimeGenre.FANTASY
        else -> AnimeGenre.TOP_AIRING
    }

@Preview(showBackground = true)
@Composable
private fun AnimeSectionPreview() {
    AnimeSection(
        titleResId = R.string.top_airing_anime,
        listUiState = ListUiState.Success(
            listOf(
                Anime(1, null, "Title 1", "Status 1", "Synopsis 1"),
                Anime(2, null, "Title 2", "Status 2", "Synopsis 2"),
                Anime(3, null, "Title 3", "Status 3", "Synopsis 3")
            )
        ),
        onSeeMoreClick = { _ -> },
        onAnimeClick = { _, _ -> },
        contentPadding = PaddingValues(32.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun AnimeListPreview() {
    AnimeList(
        titleResId = R.string.top_airing_anime,
        listOf(
            Anime(1, null, "Title 1", "Status 1", "Synopsis 1"),
            Anime(2, null, "Title 2", "Status 2", "Synopsis 2"),
            Anime(3, null, "Title 3", "Status 3", "Synopsis 3")
        ),
        { _, _ -> }, PaddingValues(32.dp)
    )
}