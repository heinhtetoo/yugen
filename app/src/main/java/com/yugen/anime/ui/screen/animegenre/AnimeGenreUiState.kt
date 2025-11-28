package com.yugen.anime.ui.screen.animegenre

import com.yugen.anime.domain.model.Anime

sealed interface AnimeGenreUiState {

    object Idle : AnimeGenreUiState
    object Loading : AnimeGenreUiState
    data class Success(val data: List<Anime>) : AnimeGenreUiState
    data class Error(val message: String, val details: String) : AnimeGenreUiState
}