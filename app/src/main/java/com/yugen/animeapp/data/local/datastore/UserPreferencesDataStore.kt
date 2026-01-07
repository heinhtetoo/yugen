package com.yugen.animeapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yugen.animeapp.domain.model.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserPreferencesKeys {

    val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    val USERNAME = stringPreferencesKey("username")
    val THEME = stringPreferencesKey("theme")
    val ANIME_GENRE_IDS = stringSetPreferencesKey("anime_genre_ids")
}

private val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreferencesDataStore(private val context: Context) {

    val isOnboardingCompleted: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[UserPreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false
        }

    suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.IS_ONBOARDING_COMPLETED] = isCompleted
        }
    }

    val usernamePreference: Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[UserPreferencesKeys.USERNAME] ?: ""
        }

    suspend fun setUsernamePreference(username: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.USERNAME] = username
        }
    }

    val themePreference: Flow<ThemePreference> =
        context.dataStore.data.map { preferences ->
            val name = preferences[UserPreferencesKeys.THEME] ?: ThemePreference.SYSTEM.name
            ThemePreference.valueOf(name)
        }

    suspend fun setThemePreference(themePreference: ThemePreference) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.THEME] = themePreference.name
        }
    }

    val animeGenrePreference: Flow<Set<String>> =
        context.dataStore.data.map { preferences ->
            preferences[UserPreferencesKeys.ANIME_GENRE_IDS] ?: emptySet()
        }

    suspend fun setAnimeGenrePreference(genreIds: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.ANIME_GENRE_IDS] = genreIds
        }
    }
}