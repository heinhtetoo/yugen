package com.yugen.anime.domain.repository

import com.yugen.anime.domain.model.ThemePreference
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    fun getIsOnboardingCompleted(): Flow<Boolean>
    suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean)

    fun getThemePreference(): Flow<ThemePreference>
    suspend fun setThemePreference(themePreference: ThemePreference)

    fun getAnimeGenrePreference(): Flow<Set<String>>
    suspend fun setAnimeGenrePreference(genrePreference: Set<String>)

    suspend fun resetOnboardingPreferences()
}