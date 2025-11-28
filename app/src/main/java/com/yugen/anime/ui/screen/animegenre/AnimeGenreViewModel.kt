package com.yugen.anime.ui.screen.animegenre

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.model.AnimeSource
import com.yugen.anime.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AnimeGenreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    val animeSource: AnimeSource = savedStateHandle["animeSource"]
        ?: error(("Missing animeSource argument"))

    val pagedAnime: Flow<PagingData<Anime>> = animeRepository.getPagedAnime(animeSource)
        .cachedIn(viewModelScope)
}