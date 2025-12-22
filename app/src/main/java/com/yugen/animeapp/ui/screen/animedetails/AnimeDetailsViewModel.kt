package com.yugen.animeapp.ui.screen.animedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.WatchStatus
import com.yugen.animeapp.domain.repository.AnimeRepository
import com.yugen.animeapp.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: AnimeRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val animeId: Int = savedStateHandle["animeId"]
        ?: error("Missing animeId argument")

    val isFavourite: StateFlow<Boolean> =
        libraryRepository.isFavouriteAnime(animeId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val watchStatus: StateFlow<WatchStatus?> =
        libraryRepository.getAnimeWatchStatus(animeId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val uiState: StateFlow<AnimeDetailsUiState> =
        combine(
            animeRepository.getAnimeDetailsById(animeId),
            isFavourite,
            watchStatus
        ) { details, favourite, watchStatus ->
            if (details == null) {
                AnimeDetailsUiState.Loading
            } else {
                AnimeDetailsUiState.Success(details, favourite, watchStatus)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AnimeDetailsUiState.Loading
        )

    private val _recommendations = MutableStateFlow<List<Anime>>(emptyList())
    val recommendations: StateFlow<List<Anime>> = _recommendations.asStateFlow()

    init {
        viewModelScope.launch {
//            val local = animeRepository.getAnimeDetailsById(animeId).first()
//            if (local == null || local.episodes == 0) {
//                refreshDetails()
//            }
            refreshDetails()
            delay(800)
            fetchRecommendations()
        }
    }

    fun refreshDetails() {
        viewModelScope.launch {
            try {
                animeRepository.fetchAnimeDetailsById(animeId)
            } catch (e: Exception) {
                val message = when (e) {
                    is HttpException -> "Network Error"
                    is IOException -> "I/O Error"
                    else -> "Something went wrong."
                }
            }
        }
    }

    fun fetchRecommendations() {
        viewModelScope.launch {
            val recommendations = animeRepository.fetchAnimeRecommendationsById(animeId)
            _recommendations.value = recommendations
        }
    }

    fun toggleFavourite() {
        viewModelScope.launch {
            if (isFavourite.value) {
                libraryRepository.removeFavouriteAnime(animeId)
            } else {
                libraryRepository.addFavouriteAnime(animeId)
            }
        }
    }

    fun updateWatchStatus(watchStatus: WatchStatus) {
        viewModelScope.launch {
            libraryRepository.setAnimeWatchStatus(
                animeId, watchStatus
            )
        }
    }

    fun removeAnimeFromLibrary() {
        viewModelScope.launch {
            libraryRepository.removeAnimeFromLibrary(animeId)
        }
    }
}