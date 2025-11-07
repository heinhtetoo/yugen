package com.yugen.anime.ui.screen.animedetails

import com.yugen.anime.data.remote.model.AnimeDetails

sealed interface AnimeDetailsUiState {

    data class Success(val animeDetails: AnimeDetails) : AnimeDetailsUiState
    data class Error(val message: String, val details: String) : AnimeDetailsUiState
    object Loading : AnimeDetailsUiState
    object Idle : AnimeDetailsUiState
}