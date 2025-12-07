package com.yugen.anime.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.anime.domain.model.Anime
import com.yugen.anime.domain.repository.AnimeRepository
import com.yugen.anime.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.getAnimeGenrePreference().collect { genres ->
                if (genres.isEmpty()) return@collect

                genres.forEach { genreString ->
                    animeRepository.getAnimeGenreById(genreString.toInt()).firstOrNull()?.let {
                        delay(700)
                        loadListSection(
                            genreId = it.id,
                            genreName = it.name,
                            localFlow = animeRepository.getAnimeListByGenreId(it.id),
                            remoteRefresh = { animeRepository.refreshAnimeListByGenreId(it.id) }
                        )
                    }
                }
            }
        }
    }

    private fun loadListSection(
        genreId: Int,
        genreName: String,
        localFlow: Flow<List<Anime>>,
        remoteRefresh: suspend () -> Unit
    ) {
        viewModelScope.launch {
            localFlow
                .onStart {
                    _uiState.update { it.updateSection(genreId, genreName, ListUiState.Loading) }
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
                    _uiState.update {
                        it.updateSection(
                            genreId,
                            genreName,
                            ListUiState.Error(message, e.message ?: "Something went wrong.")
                        )
                    }
                }
                .collect { list ->
                    val state =
                        if (list.isEmpty()) {
                            ListUiState.Error("No Data", "This section returns an empty list.")
                        } else {
                            ListUiState.Success(list)
                        }

                    _uiState.update {
                        it.updateSection(genreId, genreName, state)
                    }
                }
        }
    }
}