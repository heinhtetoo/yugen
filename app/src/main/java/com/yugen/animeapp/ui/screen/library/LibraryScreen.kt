package com.yugen.animeapp.ui.screen.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yugen.animeapp.R
import com.yugen.animeapp.core.utils.getWatchStatusColour
import com.yugen.animeapp.core.utils.getWatchStatusIcon
import com.yugen.animeapp.core.utils.getWatchStatusLabelRes
import com.yugen.animeapp.data.local.entities.AnimeEntity
import com.yugen.animeapp.data.local.model.LibraryItem
import com.yugen.animeapp.data.mapper.toAnime
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.LibraryFilter
import com.yugen.animeapp.domain.model.WatchStatus

@Composable
fun LibraryScreen(
    navigateToAnimeDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    libraryViewModel: LibraryViewModel = hiltViewModel()
) {

    val selectedFilters by libraryViewModel.selectedFilter.collectAsState()
    val libraryUiState by libraryViewModel.uiState.collectAsState()

    LibraryBody(
        libraryUiState = libraryUiState,
        selectedFilters = selectedFilters,
        onFilterClick = libraryViewModel::toggleFilter,
        onAnimeClick = navigateToAnimeDetails,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun LibraryBody(
    libraryUiState: LibraryUiState,
    selectedFilters: Set<LibraryFilter>,
    onFilterClick: (LibraryFilter) -> Unit,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (libraryUiState) {
            is LibraryUiState.Idle -> Text(stringResource(R.string.idle))
            is LibraryUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is LibraryUiState.Error -> ErrorBody(libraryUiState.message, libraryUiState.details)
            is LibraryUiState.Success -> LibraryContent(
                libraryUiState.data,
                selectedFilters,
                onFilterClick,
                onAnimeClick
            )
        }
    }
}

@Composable
fun LibraryContent(
    data: List<LibraryItem>,
    selectedFilters: Set<LibraryFilter>,
    onFilterClick: (LibraryFilter) -> Unit,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            stringResource(R.string.library_screen_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
        LibraryFilterRow(
            selectedFilters = selectedFilters,
            onFilterClick = onFilterClick,
            Modifier.fillMaxWidth()
        )
        LibraryAnimeList(
            data = data,
            onAnimeClick = onAnimeClick,
            Modifier.fillMaxSize()
        )
    }
}

@Composable
fun LibraryFilterRow(
    selectedFilters: Set<LibraryFilter>,
    onFilterClick: (LibraryFilter) -> Unit,
    modifier: Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
    ) {
        items(LibraryFilter.entries) { filter ->
            FilterChip(
                selected = selectedFilters.contains(filter),
                onClick = { onFilterClick(filter) },
                label = { Text(filter.label) },
                leadingIcon = if (selectedFilters.contains(filter)) {
                    { Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null) }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = ShapeDefaults.Medium
            )
        }
    }
}

@Composable
fun LibraryAnimeList(
    data: List<LibraryItem>,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Rounded.Inbox,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_3xlarge))
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
                Text("No anime found")
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = dimensionResource(R.dimen.anime_width_small)),
            contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        ) {
            items(data) { anime ->
                AnimeItem(
                    anime = anime,
                    onAnimeClick = { onAnimeClick(anime.animeEntity.id) }
                )
            }
        }
    }
}

@Composable
fun AnimeItem(
    anime: LibraryItem,
    onAnimeClick: (Anime) -> Unit
) {
    Box(
        modifier = Modifier.aspectRatio(
            dimensionResource(R.dimen.anime_width_normal).value
                    / dimensionResource(R.dimen.anime_height_normal).value
        )
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .clickable { onAnimeClick(anime.toAnime()) },
            elevation = CardDefaults.cardElevation()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(anime.toAnime().images?.jpg?.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_image_placeholder),
                error = painterResource(R.drawable.ic_image_placeholder),
                contentDescription = stringResource(R.string.anime_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (anime.watchStatus != null) {
            Surface(
                color = getWatchStatusColour(anime.watchStatus),
                shape = CardDefaults.shape,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Icon(
                    imageVector = getWatchStatusIcon(anime.watchStatus),
                    contentDescription = stringResource(getWatchStatusLabelRes(anime.watchStatus)),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_xsmall))
                        .size(dimensionResource(R.dimen.icon_size_xsmall))
                )
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

@Preview(showBackground = true)
@Composable
private fun AnimeListPreview() {
    LibraryBody(
        LibraryUiState.Success(
            listOf(
                LibraryItem(AnimeEntity(1, null, "Title 1"), WatchStatus.PLAN_TO_WATCH, false),
                LibraryItem(AnimeEntity(2, null, "Title 2"), WatchStatus.PLAN_TO_WATCH, false)
            ),
        ),
        setOf(LibraryFilter.PLANNED_TO_WATCH),
        {}, {}, Modifier.fillMaxSize()
    )
}