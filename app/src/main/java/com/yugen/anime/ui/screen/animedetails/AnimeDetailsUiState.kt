package com.yugen.anime.ui.screen.animedetails

import com.yugen.anime.domain.model.AnimeDetails

sealed interface AnimeDetailsUiState {

    object Idle : AnimeDetailsUiState
    object Loading : AnimeDetailsUiState
    data class Success(val animeDetails: AnimeDetails, val isFavourite: Boolean) : AnimeDetailsUiState
    data class Error(val message: String, val details: String) : AnimeDetailsUiState
}