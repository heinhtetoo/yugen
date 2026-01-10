package com.yugen.animeapp.ui.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var selectedType by mutableStateOf<String?>(null)
        private set

    var selectedStatus by mutableStateOf<String?>(null)
        private set

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<Anime>> = snapshotFlow {
        Triple(searchQuery, selectedType, selectedStatus)
    }
        .debounce(300)
        .filter { (query, _, _) -> query.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest { (query, type, status) ->
            animeRepository.searchPagedAnime(query = query, type = type, status = status)
        }
        .cachedIn(viewModelScope)

    val recentSearches = animeRepository.getRecentSearches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChanged(newQuery: String) {
        searchQuery = newQuery
    }

    fun onTypeSelected(type: String?) {
        selectedType = if (selectedType == type) null else type
    }

    fun onStatusSelected(status: String?) {
        selectedStatus = if (selectedStatus == status) null else status
    }

    fun onSearchTriggered(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                animeRepository.addSearchToHistory(query)
            }
        }
    }

    fun deleteSearchHistory(query: String) {
        viewModelScope.launch {
            animeRepository.removeSearchFromHistory(query)
        }
    }
}