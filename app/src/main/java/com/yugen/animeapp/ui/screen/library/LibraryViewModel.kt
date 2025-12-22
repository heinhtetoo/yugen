package com.yugen.animeapp.ui.screen.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.animeapp.domain.model.LibraryFilter
import com.yugen.animeapp.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val _rawLibraryItems = libraryRepository.getUserAnimeLibraryItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _selectedFilter = MutableStateFlow<Set<LibraryFilter>>(emptySet())
    val selectedFilter: StateFlow<Set<LibraryFilter>> = _selectedFilter.asStateFlow()

    val uiState: StateFlow<LibraryUiState> =
        combine(
            _rawLibraryItems,
            _selectedFilter
        ) { items, filters ->
            if (items.isEmpty()) {
                LibraryUiState.Error(
                    "Empty List",
                    "You have no anime in your library, please add an anime to the list by clicking the favourite icon or tracking with a status in the anime details."
                )
            } else {
                LibraryUiState.Success(
                    if (filters.isEmpty()) {
                        items
                    } else {
                        items.filter { item ->
                            val matchesFavourite =
                                if (filters.contains(LibraryFilter.FAVOURITE)) item.isFavourite else true

                            val statusFilters = filters.filter { it != LibraryFilter.FAVOURITE }
                                .mapNotNull { it.toWatchStatus() }

                            val matchesStatus = if (statusFilters.isEmpty()) {
                                true
                            } else {
                                item.watchStatus in statusFilters
                            }

                            matchesFavourite && matchesStatus
                        }
                    })
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LibraryUiState.Loading)

    fun toggleFilter(filter: LibraryFilter) {
        val current = _selectedFilter.value.toMutableSet()
        if (current.contains(filter)) current.remove(filter) else current.add(filter)
        _selectedFilter.value = current
    }
}