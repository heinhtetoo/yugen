package com.yugen.animeapp.ui.screen.animelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    val genreId: Int = savedStateHandle["genreId"]
        ?: error(("Missing genreId argument"))
    val title: String? = savedStateHandle["title"]

    val pagedAnime: Flow<PagingData<Anime>> = animeRepository.getPagedAnimeListByGenreId(genreId)
        .cachedIn(viewModelScope)
}