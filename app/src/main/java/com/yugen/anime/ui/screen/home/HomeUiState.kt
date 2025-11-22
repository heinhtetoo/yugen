package com.yugen.anime.ui.screen.home

import com.yugen.anime.domain.model.Anime

data class HomeUiState(
    val topAnime: ListUiState<Anime> = ListUiState.Idle,
    val awardWinningAnime: ListUiState<Anime> = ListUiState.Idle,
    val fantasyAnime: ListUiState<Anime> = ListUiState.Idle
)