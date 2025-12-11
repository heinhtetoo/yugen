package com.yugen.animeapp.ui.screen.animedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DesktopWindows
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import com.yugen.animeapp.R
import com.yugen.animeapp.domain.model.AnimeDetails
import com.yugen.animeapp.domain.model.WatchStatus

@Composable
fun AnimeDetailsScreen(
    modifier: Modifier = Modifier,
    animeDetailsViewModel: AnimeDetailsViewModel = hiltViewModel()
) {
    val animeDetailsUiState by animeDetailsViewModel.uiState.collectAsState()

    AnimeDetailsBody(
        animeDetailsUiState = animeDetailsUiState,
        onFavouriteClick = animeDetailsViewModel::toggleFavourite,
        onWatchStatusChange = animeDetailsViewModel::updateWatchStatus,
        onRemoveFromLibraryClick = animeDetailsViewModel::removeAnimeFromLibrary,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun AnimeDetailsBody(
    animeDetailsUiState: AnimeDetailsUiState,
    onFavouriteClick: () -> Unit,
    onWatchStatusChange: () -> Unit,
    onRemoveFromLibraryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (animeDetailsUiState) {
            is AnimeDetailsUiState.Idle -> Text(stringResource(R.string.idle))
            is AnimeDetailsUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is AnimeDetailsUiState.Error -> ErrorBody(
                animeDetailsUiState.message,
                animeDetailsUiState.details
            )

            is AnimeDetailsUiState.Success -> AnimeDetails(
                animeDetails = animeDetailsUiState.animeDetails,
                isFavourite = animeDetailsUiState.isFavourite,
                watchStatus = animeDetailsUiState.watchStatus,
                onFavouriteClick = onFavouriteClick,
                onWatchStatusChange = onWatchStatusChange,
                onRemoveFromLibraryClick = onRemoveFromLibraryClick,
                Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun AnimeDetails(
    animeDetails: AnimeDetails,
    isFavourite: Boolean,
    watchStatus: WatchStatus?,
    onFavouriteClick: () -> Unit,
    onWatchStatusChange: () -> Unit,
    onRemoveFromLibraryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(animeDetails.title ?: "Unknown")
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        animeDetails.titleJapanese?.let {
            Text(it)
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        }
        animeDetails.rating?.let {
            Text(it)
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        }
        animeDetails.synopsis?.let {
            Text(it, maxLines = 5)
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        }
        animeDetails.episodes?.let { episodes ->
            Text("$episodes Episodes")
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        IconButton(onClick = { onFavouriteClick() }) {
            Icon(
                imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = if (isFavourite) MaterialTheme.colorScheme.primary else LocalContentColor.current,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        IconButton(onClick = onWatchStatusChange) {
            Icon(
                imageVector = when (watchStatus) {
                    WatchStatus.PLAN_TO_WATCH -> Icons.Outlined.DesktopWindows
                    WatchStatus.WATCHING -> Icons.Outlined.CheckCircle
                    WatchStatus.COMPLETED -> Icons.Filled.CheckCircle
                    else -> Icons.Outlined.LibraryAdd
                },
                contentDescription = null,
                tint = if (watchStatus == WatchStatus.COMPLETED) MaterialTheme.colorScheme.primary else LocalContentColor.current,
                modifier = Modifier.size(48.dp)
            )
        }

        watchStatus?.let {
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
            IconButton(onClick = onRemoveFromLibraryClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = LocalContentColor.current,
                    modifier = Modifier.size(48.dp)
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
        details?.let { Text(it, maxLines = 2) }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeDetailsPreview(modifier: Modifier = Modifier) {
    AnimeDetails(
        AnimeDetails(
            id = 0, title = "Anime Details",
            images = null,
            titleEnglish = "", titleJapanese = "",
            type = "", episodes = 3,
            status = "", rating = "",
            synopsis = "Synopsis"
        ),
        isFavourite = true,
        watchStatus = null,
        onFavouriteClick = { },
        onWatchStatusChange = { },
        onRemoveFromLibraryClick = { },
        modifier
            .height(640.dp)
            .width(320.dp)
    )
}