package com.yugen.anime.ui.screen.home

import com.yugen.anime.domain.model.Anime

data class HomeUiState(
    val sections: List<GenreSectionUiState<Anime>> = emptyList()
)

fun HomeUiState.updateSection(
    genreId: Int,
    genreName: String,
    newState: ListUiState<Anime>
): HomeUiState {
    val updatedSections =
        sections.map { section ->
            if (section.genreId == genreId) section.copy(state = newState)
            else section
        }.let { mapped ->
            if (mapped.none { it.genreId == genreId }) {
                mapped + GenreSectionUiState(genreId, genreName, newState)
            } else mapped
        }

    return copy(sections = updatedSections)
}