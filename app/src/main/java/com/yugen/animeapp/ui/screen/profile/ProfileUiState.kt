package com.yugen.animeapp.ui.screen.profile

import com.yugen.animeapp.data.local.model.GenreStat

sealed interface ProfileUiState {
    object Idle : ProfileUiState
    object Loading : ProfileUiState
    data class Success(
        val username: String = "",
        val totalAnime: Int = 0,
        val completedCount: Int = 0,
        val watchingCount: Int = 0,
        val plannedCount: Int = 0,
        val topGenres: List<GenreStat> = emptyList()
    ) : ProfileUiState

    data class Error(val message: String, val details: String) : ProfileUiState
}