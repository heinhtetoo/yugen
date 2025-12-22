package com.yugen.animeapp.ui.screen.library

import com.yugen.animeapp.data.local.model.LibraryItem

sealed interface LibraryUiState {

    object Idle : LibraryUiState
    object Loading : LibraryUiState
    data class Success(val data: List<LibraryItem>) : LibraryUiState
    data class Error(val message: String, val details: String) : LibraryUiState
}