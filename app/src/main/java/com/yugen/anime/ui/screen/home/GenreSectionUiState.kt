package com.yugen.anime.ui.screen.home

data class GenreSectionUiState<out T>(
    val genreId: Int,
    val genreName: String,
    val state: ListUiState<T>
)