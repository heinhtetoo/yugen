package com.yugen.anime.ui.screen.top

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
class HomeViewModel @Inject constructor(
    private val jikanRepository: JikanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchTopAnime()
    }

    fun fetchTopAnime() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val response = jikanRepository.fetchTopAnime()
                if (response.data.isNullOrEmpty()) {
                    _uiState.value = HomeUiState.Error(
                        "Top anime list is currently empty",
                        "The server responds with an empty top anime list."
                    )
                } else {
                    _uiState.value = HomeUiState.Success(response.data)
                }
            } catch (e: HttpException) {
                _uiState.value =
                    HomeUiState.Error("Network Error", e.message ?: "Unknown Error")
            } catch (e: IOException) {
                _uiState.value =
                    HomeUiState.Error("I/O Error", e.message ?: "Unknown Error")
            }
        }
    }
}