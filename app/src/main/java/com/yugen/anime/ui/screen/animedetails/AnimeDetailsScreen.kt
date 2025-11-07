package com.yugen.anime.ui.screen.animedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yugen.anime.R
import com.yugen.anime.data.remote.model.AnimeDetails

@Composable
fun AnimeDetailsScreen(
    modifier: Modifier = Modifier,
    animeDetailsViewModel: AnimeDetailsViewModel = hiltViewModel()
) {
    val animeDetailsUiState by animeDetailsViewModel.uiState.collectAsState()

    AnimeDetailsBody(
        animeDetailsUiState = animeDetailsUiState,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun AnimeDetailsBody(
    animeDetailsUiState: AnimeDetailsUiState,
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
                Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun AnimeDetails(
    animeDetails: AnimeDetails,
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
        Text("${animeDetails.episodes} Episodes")
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