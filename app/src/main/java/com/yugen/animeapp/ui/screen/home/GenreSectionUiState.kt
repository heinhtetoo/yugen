package com.yugen.animeapp.ui.screen.home

data class GenreSectionUiState<out T>(
    val genreId: Int,
    val genreName: String?,
    val titleRes: Int?,
    val state: ListUiState<T>
)