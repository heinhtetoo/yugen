package com.yugen.animeapp.data.repository

import com.yugen.animeapp.data.local.datastore.UserPreferencesDataStore
import com.yugen.animeapp.domain.model.ThemePreference
import com.yugen.animeapp.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : UserPreferencesRepository {

    override fun getIsOnboardingCompleted(): Flow<Boolean> =
        dataStore.isOnboardingCompleted

    override suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean) =
        dataStore.setOnboardingCompleted(isOnboardingCompleted)

    override fun getUsernamePreference(): Flow<String> =
        dataStore.usernamePreference

    override suspend fun setUsernamePreference(username: String) =
        dataStore.setUsernamePreference(username)

    override fun getThemePreference(): Flow<ThemePreference> =
        dataStore.themePreference

    override suspend fun setThemePreference(themePreference: ThemePreference) =
        dataStore.setThemePreference(themePreference)

    override fun getAnimeGenrePreference(): Flow<Set<String>> =
        dataStore.animeGenrePreference

    override suspend fun setAnimeGenrePreference(genrePreference: Set<String>) =
        dataStore.setAnimeGenrePreference(genrePreference)

    override suspend fun resetOnboardingPreferences() {
        setAnimeGenrePreference(emptySet())
        setThemePreference(ThemePreference.SYSTEM)
        setUsernamePreference("")
        setIsOnboardingCompleted(false)
    }
}