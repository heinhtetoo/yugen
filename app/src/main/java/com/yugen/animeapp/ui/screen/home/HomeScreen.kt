package com.yugen.animeapp.ui.screen.home

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
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material.icons.automirrored.rounded.Forward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yugen.animeapp.R
import com.yugen.animeapp.domain.model.Anime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSearch: () -> Unit,
    navigateToAnimeList: (Int) -> Unit,
    navigateToAnimeDetails: (Int) -> Unit,
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
    onSeeMoreClick: (Int) -> Unit,
    onAnimeClick: (Int) -> Unit,
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
                painter = painterResource(R.drawable.ic_yugen),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_xlarge)),
                tint = Color.Unspecified,
            )
            Text(
                stringResource(R.string.home_screen_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.search_anime),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_large))
                    .clickable(onClick = onSearchClick)
            )
        }
        homeUiState.sections.forEach { section ->
            AnimeSection(
                genreId = section.genreId,
                genreName = section.genreName,
                titleRes = section.titleRes,
                listUiState = section.state,
                onSeeMoreClick = onSeeMoreClick,
                onAnimeClick = onAnimeClick,
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
fun AnimeSection(
    genreId: Int,
    genreName: String?,
    titleRes: Int?,
    listUiState: ListUiState<Anime>,
    onSeeMoreClick: (Int) -> Unit,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val title =
        titleRes?.let { stringResource(it) } ?: genreName
        ?: stringResource(R.string.anime_section_title_placeholder)

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
                title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Normal
            )
            if (genreId >= 0) {
                TextButton(
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small)),
                    onClick = { onSeeMoreClick(genreId) }) {
                    Text(
                        stringResource(R.string.see_all),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
        when (listUiState) {
            is ListUiState.Idle -> Text(
                stringResource(R.string.idle),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .height(
                        dimensionResource(
                            if (titleRes == R.string.top_airing_anime) R.dimen.anime_height_xlarge
                            else R.dimen.anime_height_normal
                        )
                    )
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

            is ListUiState.Loading -> Box(
                Modifier
                    .fillMaxWidth()
                    .height(
                        dimensionResource(
                            if (titleRes == R.string.top_airing_anime) R.dimen.anime_height_xlarge
                            else R.dimen.anime_height_normal
                        )
                    ),
                Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is ListUiState.Error -> ErrorBody(
                listUiState.message, listUiState.details,
                Modifier
                    .fillMaxWidth()
                    .height(
                        dimensionResource(
                            if (titleRes == R.string.top_airing_anime) R.dimen.anime_height_xlarge
                            else R.dimen.anime_height_normal
                        )
                    )
            )

            is ListUiState.Success -> AnimeList(
                title = title,
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
    title: String,
    data: List<Anime>,
    onAnimeClick: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        contentPadding = contentPadding
    ) {
        items(data) { anime ->
            AnimeItem(
                anime = anime,
                onAnimeClick = { onAnimeClick(anime.id) },
                modifier = Modifier
                    .size(
                        dimensionResource(
                            if (title == stringResource(R.string.top_airing_anime)) R.dimen.anime_width_xlarge
                            else R.dimen.anime_width_normal
                        ),
                        dimensionResource(
                            if (title == stringResource(R.string.top_airing_anime)) R.dimen.anime_height_xlarge
                            else R.dimen.anime_height_normal
                        )
                    )
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
            placeholder = painterResource(R.drawable.ic_image_placeholder),
            error = painterResource(R.drawable.ic_image_placeholder),
            contentDescription = stringResource(R.string.anime_poster),
            contentScale = ContentScale.FillHeight,
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

@Preview(showBackground = true)
@Composable
private fun HomeBodyAnimePreview() {
    val animeList = listOf(
        GenreSectionUiState(
            genreId = 1,
            genreName = stringResource(R.string.top_airing_anime),
            titleRes = null,
            state = ListUiState.Success(
                listOf(
                    Anime(1, null, "Title 1", "Status 1", "Synopsis 1"),
                    Anime(2, null, "Title 2", "Status 2", "Synopsis 2"),
                    Anime(3, null, "Title 3", "Status 3", "Synopsis 3")
                )
            )
        )
    )
    HomeBody(HomeUiState(animeList), {}, {}, {}, Modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
private fun HomeBodyIdlePreview() {
    val animeList = listOf(
        GenreSectionUiState(
            genreId = 1,
            genreName = stringResource(R.string.top_airing_anime),
            titleRes = null,
            state = ListUiState.Idle
        )
    )
    HomeBody(HomeUiState(animeList), {}, {}, {}, Modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
private fun HomeBodyLoadingPreview() {
    val animeList = listOf(
        GenreSectionUiState(
            genreId = 1,
            genreName = stringResource(R.string.top_airing_anime),
            titleRes = null,
            state = ListUiState.Loading
        )
    )
    HomeBody(HomeUiState(animeList), {}, {}, {}, Modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
private fun HomeBodyErrorPreview() {
    val animeList = listOf(
        GenreSectionUiState(
            genreId = 1,
            genreName = stringResource(R.string.top_airing_anime),
            titleRes = null,
            state = ListUiState.Error("Error", "Details")
        )
    )
    HomeBody(HomeUiState(animeList), {}, {}, {}, Modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
private fun AnimeSectionPreview() {
    AnimeSection(
        genreId = 1,
        genreName = stringResource(R.string.top_airing_anime),
        titleRes = null,
        listUiState = ListUiState.Success(
            listOf(
                Anime(1, null, "Title 1", "Status 1", "Synopsis 1"),
                Anime(2, null, "Title 2", "Status 2", "Synopsis 2"),
                Anime(3, null, "Title 3", "Status 3", "Synopsis 3")
            )
        ),
        onSeeMoreClick = { _ -> },
        onAnimeClick = { _ -> },
        contentPadding = PaddingValues(32.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun AnimeListPreview() {
    AnimeList(
        title = stringResource(R.string.top_airing_anime),
        listOf(
            Anime(1, null, "Title 1", "Status 1", "Synopsis 1"),
            Anime(2, null, "Title 2", "Status 2", "Synopsis 2"),
            Anime(3, null, "Title 3", "Status 3", "Synopsis 3")
        ),
        { _ -> }, PaddingValues(32.dp)
    )
}