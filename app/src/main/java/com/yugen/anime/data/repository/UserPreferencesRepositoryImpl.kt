package com.yugen.anime.data.repository

import com.yugen.anime.data.local.datastore.UserPreferencesDataStore
import com.yugen.anime.domain.model.ThemePreference
import com.yugen.anime.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : UserPreferencesRepository {

    override fun getIsOnboardingCompleted(): Flow<Boolean> =
        dataStore.isOnboardingCompleted

    override suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean) =
        dataStore.setOnboardingCompleted(isOnboardingCompleted)

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
        setIsOnboardingCompleted(false)
    }
}