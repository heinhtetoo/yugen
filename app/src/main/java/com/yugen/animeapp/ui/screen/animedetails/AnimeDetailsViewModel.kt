package com.yugen.animeapp.ui.screen.animedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.animeapp.domain.model.WatchStatus
import com.yugen.animeapp.domain.repository.AnimeRepository
import com.yugen.animeapp.domain.repository.FavouriteAnimeRepository
import com.yugen.animeapp.domain.repository.UserAnimeLibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: AnimeRepository,
    private val favouriteAnimeRepository: FavouriteAnimeRepository,
    private val libraryRepository: UserAnimeLibraryRepository
) : ViewModel() {

    private val animeId: Int = savedStateHandle["animeId"]
        ?: error("Missing animeId argument")

    val isFavourite: StateFlow<Boolean> =
        favouriteAnimeRepository.isFavouriteAnime(animeId)
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

    init {
        viewModelScope.launch {
            val local = animeRepository.getAnimeDetailsById(animeId).first()
            if (local == null || local.episodes == 0) {
                refreshDetails()
            }
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

    fun toggleFavourite() {
        viewModelScope.launch {
            if (isFavourite.value) {
                favouriteAnimeRepository.removeFavouriteAnime(animeId)
            } else {
                favouriteAnimeRepository.addFavouriteAnime(animeId)
            }
        }
    }

    fun updateWatchStatus() {
        viewModelScope.launch {
            libraryRepository.setAnimeWatchStatus(
                animeId, when (watchStatus.value) {
                    null -> WatchStatus.PLAN_TO_WATCH
                    WatchStatus.PLAN_TO_WATCH -> WatchStatus.WATCHING
                    WatchStatus.WATCHING -> WatchStatus.COMPLETED
                    else -> WatchStatus.COMPLETED
                }
            )
        }
    }

    fun removeAnimeFromLibrary() {
        viewModelScope.launch {
            libraryRepository.removeAnimeFromLibrary(animeId)
        }
    }
}