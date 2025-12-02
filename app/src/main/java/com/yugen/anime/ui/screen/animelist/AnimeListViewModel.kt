package com.yugen.anime.ui.screen.animelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeGenre
import com.yugen.anime.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    val animeGenre: AnimeGenre = savedStateHandle["animeGenre"]
        ?: error(("Missing animeGenre argument"))

    val pagedAnime: Flow<PagingData<Anime>> = animeRepository.getPagedAnimeListByGenreId(animeGenre)
        .cachedIn(viewModelScope)
}