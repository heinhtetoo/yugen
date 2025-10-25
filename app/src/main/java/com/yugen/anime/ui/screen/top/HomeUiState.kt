package com.yugen.anime.ui.screen.top

import com.yugen.anime.data.remote.model.Anime

sealed interface HomeUiState {

    data class Success(val data: List<Anime>) : HomeUiState
    data class Error(val message: String, val details: String) : HomeUiState
    object Loading : HomeUiState
    object Idle : HomeUiState
}