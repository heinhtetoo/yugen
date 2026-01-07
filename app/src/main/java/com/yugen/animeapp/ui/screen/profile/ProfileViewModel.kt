package com.yugen.animeapp.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.animeapp.domain.model.ThemePreference
import com.yugen.animeapp.domain.model.WatchStatus
import com.yugen.animeapp.domain.repository.LibraryRepository
import com.yugen.animeapp.domain.repository.UserPreferencesRepository
import com.yugen.animeapp.ui.screen.library.LibraryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val theme = userPreferencesRepository.getThemePreference()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemePreference.SYSTEM)

    private val _username = userPreferencesRepository.getUsernamePreference()
    private val _watchStatusCounts = libraryRepository.getLibraryWatchStatusCounts()
    private val _topGenres = libraryRepository.getTopLibraryGenres()

    val uiState: StateFlow<ProfileUiState> =
        combine(
            _username,
            _watchStatusCounts,
            _topGenres
        ) { username, watchStatusCounts, topGenres ->
            val countsMap = watchStatusCounts.associate { it.statusId to it.count }

            ProfileUiState.Success(
                username = username,
                totalAnime = countsMap.values.sum(),
                completedCount = countsMap[WatchStatus.COMPLETED.id] ?: 0,
                watchingCount = countsMap[WatchStatus.WATCHING.id] ?: 0,
                plannedCount = countsMap[WatchStatus.PLAN_TO_WATCH.id] ?: 0,
                topGenres = topGenres
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState.Loading)

    fun updateUsername(username: String) {
        viewModelScope.launch {
            userPreferencesRepository.setUsernamePreference(username)
        }
    }

    fun updateTheme(themePreference: ThemePreference) {
        viewModelScope.launch {
            userPreferencesRepository.setThemePreference(themePreference)
        }
    }
}