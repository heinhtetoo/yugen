package com.yugen.anime.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.anime.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadListSection(
            localFlow = animeRepository.getAnimeListByGenreId(1),
            remoteRefresh = { animeRepository.refreshAnimeListByGenreId(1) },
            onStateChange = { state -> _uiState.update { it.copy(topAiringAnime = state) } }
        )
        loadListSection(
            localFlow = animeRepository.getAnimeListByGenreId(2),
            remoteRefresh = { animeRepository.refreshAnimeListByGenreId(2) },
            onStateChange = { state -> _uiState.update { it.copy(topUpcomingAnime = state) } }
        )
        loadListSection(
            localFlow = animeRepository.getAnimeListByGenreId(42),
            remoteRefresh = { animeRepository.refreshAnimeListByGenreId(42) },
            onStateChange = { state -> _uiState.update { it.copy(awardWinningAnime = state) } }
        )
        loadListSection(
            localFlow = animeRepository.getAnimeListByGenreId(10),
            remoteRefresh = { animeRepository.refreshAnimeListByGenreId(10) },
            onStateChange = { state -> _uiState.update { it.copy(fantasyAnime = state) } }
        )
    }

    private fun <T> loadListSection(
        localFlow: Flow<List<T>>,
        remoteRefresh: suspend () -> Unit,
        onStateChange: (ListUiState<T>) -> Unit
    ) {
        viewModelScope.launch {
            localFlow
                .onStart {
                    onStateChange(ListUiState.Loading)
                    try {
                        remoteRefresh()
                    } catch (_: Exception) {
                    }
                }
                .catch { e ->
                    val message = when (e) {
                        is HttpException -> "Network Error"
                        is IOException -> "I/O Error"
                        else -> "Unknown Error"
                    }
                    onStateChange(
                        ListUiState.Error(message, e.message ?: "Something went wrong.")
                    )
                }
                .collect { list ->
                    if (list.isEmpty()) {
                        onStateChange(
                            ListUiState.Error(
                                "No Data", "This section returns an empty list."
                            )
                        )
                    } else {
                        onStateChange(ListUiState.Success(list))
                    }
                }
        }
    }
}