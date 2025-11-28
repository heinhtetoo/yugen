package com.yugen.anime.ui.screen.favourite

import com.yugen.anime.domain.model.Anime

sealed interface FavouriteAnimeUiState {

    object Idle : FavouriteAnimeUiState
    object Loading : FavouriteAnimeUiState
    data class Success(val data: List<Anime>) : FavouriteAnimeUiState
    data class Error(val message: String, val details: String) : FavouriteAnimeUiState
}