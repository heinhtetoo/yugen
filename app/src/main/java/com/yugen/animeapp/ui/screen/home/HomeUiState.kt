package com.yugen.animeapp.ui.screen.home

import com.yugen.animeapp.domain.model.Anime

data class HomeUiState(
    val sections: List<GenreSectionUiState<Anime>> = emptyList()
)

fun HomeUiState.updateSection(
    genreId: Int,
    genreName: String?,
    titleRes: Int?,
    newState: ListUiState<Anime>
): HomeUiState {
    val updatedSections =
        sections.map { section ->
            if (section.genreId == genreId) section.copy(state = newState)
            else section
        }.let { mapped ->
            if (mapped.none { it.genreId == genreId }) {
                mapped + GenreSectionUiState(genreId, genreName, titleRes, newState)
            } else mapped
        }

    return copy(sections = updatedSections)
}