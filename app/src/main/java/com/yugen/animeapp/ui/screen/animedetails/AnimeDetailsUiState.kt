package com.yugen.animeapp.ui.screen.animedetails

import com.yugen.animeapp.domain.model.AnimeDetails
import com.yugen.animeapp.domain.model.WatchStatus

sealed interface AnimeDetailsUiState {

    object Idle : AnimeDetailsUiState
    object Loading : AnimeDetailsUiState
    data class Success(val animeDetails: AnimeDetails, val isFavourite: Boolean, val watchStatus: WatchStatus?) : AnimeDetailsUiState
    data class Error(val message: String, val details: String) : AnimeDetailsUiState
}