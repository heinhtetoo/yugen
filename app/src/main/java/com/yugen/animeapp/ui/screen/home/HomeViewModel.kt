package com.yugen.animeapp.ui.screen.home

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.DefaultHomeSectionType
import com.yugen.animeapp.domain.repository.AnimeRepository
import com.yugen.animeapp.domain.repository.UserPreferencesRepository
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
            userPreferencesRepository.getAnimeGenrePreference().collect { userGenres ->
                if (userGenres.isEmpty()) return@collect

//                userGenres.forEach { genreString ->
//                    animeRepository.getAnimeGenreById(genreString.toInt()).firstOrNull()?.let {
//                        delay(700)
//                        loadListSection(
//                            genreId = it.id,
//                            genreName = it.name,
//                            localFlow = animeRepository.getAnimeListByGenreId(it.id),
//                            remoteRefresh = { animeRepository.refreshAnimeListByGenreId(it.id) }
//                        )
//                    }
//                }

                buildSections(userGenres)
            }
        }
    }

    private suspend fun buildSections(userGenres: Set<String>) {
        val userGenreConfigs = userGenres.mapNotNull { genreIdString ->
            animeRepository.getAnimeGenreById(genreIdString.toInt()).firstOrNull()?.let {
                HomeSectionConfig.UserGenre(it.id, it.name)
            }
        }

        val homeSections = mutableListOf<HomeSectionConfig>()

        homeSections.add(
            HomeSectionConfig.Default(
                genreId = DefaultHomeSectionType.TOP_AIRING.genreId,
                titleRes = DefaultHomeSectionType.TOP_AIRING.titleRes,
                type = DefaultHomeSectionType.TOP_AIRING
            )
        )

        if (userGenreConfigs.isNotEmpty()) {
            homeSections.add(userGenreConfigs[0])
        }

        homeSections.add(
            HomeSectionConfig.Default(
                genreId = DefaultHomeSectionType.TOP_UPCOMING.genreId,
                titleRes = DefaultHomeSectionType.TOP_UPCOMING.titleRes,
                type = DefaultHomeSectionType.TOP_UPCOMING
            )
        )

        if (userGenreConfigs.size > 1) {
            homeSections.add(userGenreConfigs[1])
        }

        homeSections.add(
            HomeSectionConfig.Default(
                genreId = DefaultHomeSectionType.AWARD_WINNING.genreId,
                titleRes = DefaultHomeSectionType.AWARD_WINNING.titleRes,
                type = DefaultHomeSectionType.AWARD_WINNING
            )
        )

        if (userGenreConfigs.size > 2) {
            homeSections.addAll(userGenreConfigs.drop(2))
        }

        homeSections.forEach { section ->
            delay(1000)

            when (section) {
                is HomeSectionConfig.Default -> {
                    loadListSection(
                        genreId = section.genreId,
                        genreName = null,
                        titleRes = section.titleRes,
                        localFlow = animeRepository.getDefaultAnimeListByType(section.type),
                        remoteRefresh = { animeRepository.refreshDefaultAnimeListByType(section.type) }
                    )
                }

                is HomeSectionConfig.UserGenre -> {
                    loadListSection(
                        genreId = section.genreId,
                        genreName = section.title,
                        titleRes = null,
                        localFlow = animeRepository.getAnimeListByGenreId(section.genreId),
                        remoteRefresh = { animeRepository.refreshAnimeListByGenreId(section.genreId) }
                    )
                }
            }

        }
    }

    private fun loadListSection(
        genreId: Int,
        genreName: String?,
        titleRes: Int?,
        localFlow: Flow<List<Anime>>,
        remoteRefresh: suspend () -> Unit
    ) {
        viewModelScope.launch {
            localFlow
                .onStart {
                    _uiState.update { it.updateSection(genreId, genreName, titleRes, ListUiState.Loading) }
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
                            titleRes,
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
                        it.updateSection(genreId, genreName, titleRes, state)
                    }
                }
        }
    }
}