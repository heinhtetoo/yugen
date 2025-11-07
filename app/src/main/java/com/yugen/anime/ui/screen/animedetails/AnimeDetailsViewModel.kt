package com.yugen.anime.ui.screen.animedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.anime.domain.repository.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val jikanRepository: JikanRepository
) : ViewModel() {

    private val animeId: Int = savedStateHandle["animeId"]
        ?: error("Missing animeId argument")

    private val _uiState = MutableStateFlow<AnimeDetailsUiState>(AnimeDetailsUiState.Idle)
    val uiState: StateFlow<AnimeDetailsUiState> = _uiState.asStateFlow()

    init {
        getAnimeDetailsById()
    }

    fun getAnimeDetailsById() {
        viewModelScope.launch {
            _uiState.value = AnimeDetailsUiState.Loading
            try {
                val response = jikanRepository.getAnimeDetailsById(animeId = animeId)
                if (response.data == null) {
                    _uiState.value = AnimeDetailsUiState.Error(
                        "No Anime Details",
                        "The server responds with an empty anime details."
                    )
                } else {
                    _uiState.value = AnimeDetailsUiState.Success(response.data)
                }
            } catch (e: Exception) {
                val message = when (e) {
                    is HttpException -> "Network Error"
                    is IOException -> "I/O Error"
                    else -> "Something went wrong."
                }
                _uiState.value = AnimeDetailsUiState.Error(message, e.message ?: "Unknown Error")
            }
        }
    }
}