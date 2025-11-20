package com.yugen.anime.domain.repository

import com.yugen.anime.domain.model.ThemePreference
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    fun getThemePreference(): Flow<ThemePreference>
    suspend fun setThemePreference(themePreference: ThemePreference)
}