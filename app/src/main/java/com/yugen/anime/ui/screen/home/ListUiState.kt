package com.yugen.anime.ui.screen.home

sealed interface ListUiState<out T> {

    data object Idle : ListUiState<Nothing>
    data object Loading : ListUiState<Nothing>
    data class Success<T>(val data: List<T>) : ListUiState<T>
    data class Error(val message: String, val details: String) : ListUiState<Nothing>
}