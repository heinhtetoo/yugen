package com.yugen.anime.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
import com.yugen.anime.domain.model.AnimeSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAnimeDetails: (Int, AnimeSource) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by homeViewModel.uiState.collectAsState()

    HomeBody(
        homeUiState = homeUiState,
        onAnimeClick = navigateToAnimeDetails,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium))
    )
}

@Composable
fun HomeBody(
    homeUiState: HomeUiState,
    onAnimeClick: (Int, AnimeSource) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        AnimeSection(
            R.string.top_anime,
            homeUiState.topAnime,
            onAnimeClick,
            contentPadding = contentPadding
        )
        AnimeSection(
            R.string.award_winning_anime,
            homeUiState.awardWinningAnime,
            onAnimeClick,
            contentPadding = contentPadding
        )
        AnimeSection(
            R.string.fantasy_anime,
            homeUiState.fantasyAnime,
            onAnimeClick,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun AnimeSection(
    titleResId: Int,
    listUiState: ListUiState<Anime>,
    onAnimeClick: (Int, AnimeSource) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(stringResource(titleResId), fontWeight = FontWeight.SemiBold)
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
    onAnimeClick: (Int, AnimeSource) -> Unit,
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
                onAnimeClick = { onAnimeClick(anime.id, getAnimeSource(titleResId)) },
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
//        Row(Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
//            Column {
//                Text(anime.title ?: "Unknown", fontWeight = FontWeight.Bold)
//                anime.status?.let { Text(it) }
//                anime.synopsis?.let { Text(it, maxLines = 5) }
//            }
//        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(anime.images?.jpg?.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.round_image_24),
            error = painterResource(R.drawable.round_broken_image_24),
            contentDescription = anime.title,
            contentScale = ContentScale.Fit,
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
    if (titleResId == R.string.award_winning_anime) R.dimen.anime_width_large
    else R.dimen.anime_width_normal

private fun getAnimeHeightDimenResId(titleResId: Int) =
    if (titleResId == R.string.award_winning_anime) R.dimen.anime_height_large
    else R.dimen.anime_height_normal

private fun getAnimeSource(titleResId: Int) =
    when (titleResId) {
        R.string.top_anime -> AnimeSource.TOP
        R.string.award_winning_anime -> AnimeSource.AWARD_WINNING
        R.string.fantasy_anime -> AnimeSource.FANTASY
        else -> AnimeSource.TOP
    }

@Preview
@Composable
private fun AnimeListPreview() {
    AnimeList(
        titleResId = R.string.top_anime,
        listOf(
            Anime(1, null, "Title 1", "Status 1", "Synopsis 1"),
            Anime(2, null, "Title 2", "Status 2", "Synopsis 2"),
            Anime(3, null, "Title 3", "Status 3", "Synopsis 3")
        ),
        { _, _ -> }, PaddingValues(32.dp)
    )
}