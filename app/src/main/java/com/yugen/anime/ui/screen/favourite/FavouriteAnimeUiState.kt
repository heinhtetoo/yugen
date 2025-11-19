package com.yugen.anime.ui.screen.favourite

import com.yugen.anime.domain.model.Anime

sealed interface FavouriteAnimeUiState {

    data class Success(val data: List<Anime>) : FavouriteAnimeUiState
    data class Error(val message: String, val details: String) : FavouriteAnimeUiState
    object Loading : FavouriteAnimeUiState
    object Idle : FavouriteAnimeUiState
}