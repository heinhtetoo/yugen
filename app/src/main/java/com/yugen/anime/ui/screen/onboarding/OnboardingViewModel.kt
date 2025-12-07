package com.yugen.anime.ui.screen.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugen.anime.domain.model.OnboardingStep
import com.yugen.anime.domain.model.ThemePreference
import com.yugen.anime.domain.repository.AnimeRepository
import com.yugen.anime.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    var currentStep by mutableStateOf(OnboardingStep.THEME_SELECTION)
        private set

    val selectedTheme = userPreferencesRepository.getThemePreference()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemePreference.SYSTEM)

    val genreList = animeRepository.getAnimeGenres()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _selectedGenreIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedGenreIds = _selectedGenreIds.asStateFlow()

    init {
        viewModelScope.launch {
            animeRepository.refreshAnimeGenresIfNecessary()
        }
    }

    fun onThemeSelected(theme: ThemePreference) {
        viewModelScope.launch {
            userPreferencesRepository.setThemePreference(theme)
        }
    }

    fun onGenreToggled(genreId: Int, max: Int) {
        _selectedGenreIds.value = _selectedGenreIds.value.toMutableSet().apply {
            if (contains(genreId)) remove(genreId) else {
                if (size < max) add(genreId)
            }
        }
    }

    fun nextStep() {
        when (currentStep) {
            OnboardingStep.THEME_SELECTION -> currentStep = OnboardingStep.GENRE_SELECTION
            OnboardingStep.GENRE_SELECTION -> finishOnboarding()
        }
    }

    fun previousStep() {
        if (currentStep == OnboardingStep.GENRE_SELECTION)
            currentStep = OnboardingStep.THEME_SELECTION
    }

    private fun finishOnboarding() {
        viewModelScope.launch {
            userPreferencesRepository.apply {
                setAnimeGenrePreference(selectedGenreIds.value.map { it.toString() }.toSet())
                setIsOnboardingCompleted(true)
            }
        }
    }
}