package com.yugen.anime.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.anime.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeLocalData()
        refreshFromRemote()
    }

    private fun observeLocalData() {
        viewModelScope.launch {
            animeRepository.getTopAnime().collect { topAnime ->
                if (topAnime.isEmpty()) {
                    _uiState.value = HomeUiState.Loading
                } else {
                    _uiState.value = HomeUiState.Success(topAnime)
                }
            }
        }
    }

    fun refreshFromRemote() {
        viewModelScope.launch {
            try {
                animeRepository.refreshTopAnime()
            } catch (e: Exception) {
                val message = when (e) {
                    is HttpException -> "Network Error"
                    is IOException -> "I/O Error"
                    else -> "Unknown Error"
                }
                _uiState.value =
                    HomeUiState.Error(message, e.message ?: "Unknown Error")
            }
        }
    }
}