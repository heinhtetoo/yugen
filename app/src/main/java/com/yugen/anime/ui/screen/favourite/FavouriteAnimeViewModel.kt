package com.yugen.anime.ui.screen.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.anime.domain.repository.AnimeRepository
import com.yugen.anime.domain.repository.FavouriteAnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteAnimeViewModel @Inject constructor(
    private val favouriteAnimeRepository: FavouriteAnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavouriteAnimeUiState>(FavouriteAnimeUiState.Idle)
    val uiState: StateFlow<FavouriteAnimeUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            favouriteAnimeRepository.getFavouriteAnime().collect { favouriteAnime ->
                if (favouriteAnime.isEmpty()) {
                    _uiState.value = FavouriteAnimeUiState.Error(
                        "Empty List",
                        "You have no favourite anime, please add an anime to favourite list by clicking the favourite icon in the anime details."
                    )
                } else {
                    _uiState.value = FavouriteAnimeUiState.Success(favouriteAnime)
                }
            }
        }
    }
}