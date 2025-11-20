package com.yugen.anime.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yugen.anime.domain.model.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreferencesDataStore(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_mode")
    }

    val themePreference: Flow<ThemePreference> =
        context.dataStore.data.map { preferences ->
            val name = preferences[THEME_KEY] ?: ThemePreference.SYSTEM.name
            ThemePreference.valueOf(name)
        }

    suspend fun setThemePreference(themePreference: ThemePreference) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themePreference.name
        }
    }
}