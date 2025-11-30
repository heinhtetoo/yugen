package com.yugen.anime.ui.screen.animedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.anime.domain.model.AnimeCategory
import com.yugen.anime.domain.repository.AnimeRepository
import com.yugen.anime.domain.repository.FavouriteAnimeRepository
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
    private val favouriteAnimeRepository: FavouriteAnimeRepository
) : ViewModel() {

    private val animeId: Int = savedStateHandle["animeId"]
        ?: error("Missing animeId argument")
    private val animeCategory: AnimeCategory = savedStateHandle["animeCategory"]
        ?: error(("Missing animeCategory argument"))

    val isFavourite: StateFlow<Boolean> =
        favouriteAnimeRepository.isFavourite(animeId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val uiState: StateFlow<AnimeDetailsUiState> =
        combine(
            animeRepository.getAnimeDetailsById(animeId),
            isFavourite
        ) { details, favourite ->
            if (details == null) {
                AnimeDetailsUiState.Loading
            } else {
                AnimeDetailsUiState.Success(details, favourite)
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
                animeRepository
                    .fetchAnimeDetailsById(
                        animeId,
                        animeCategory == AnimeCategory.FAVORITE,
                        animeCategory == AnimeCategory.TOP_AIRING,
                        animeCategory == AnimeCategory.TOP_UPCOMING,
                        animeCategory == AnimeCategory.AWARD_WINNING,
                        animeCategory == AnimeCategory.FANTASY,
                    )
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
//        Log.e("FAV", isFavourite.toString())
        viewModelScope.launch {
            if (isFavourite.value) {
                favouriteAnimeRepository.removeFavouriteAnime(animeId)
            } else {
                favouriteAnimeRepository.addFavouriteAnime(animeId)
            }
        }
    }
}