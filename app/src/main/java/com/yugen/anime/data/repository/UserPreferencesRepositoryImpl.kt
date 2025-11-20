package com.yugen.anime.data.repository

import com.yugen.anime.data.local.datastore.UserPreferencesDataStore
import com.yugen.anime.domain.model.ThemePreference
import com.yugen.anime.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : UserPreferencesRepository {

    override fun getThemePreference(): Flow<ThemePreference> =
        dataStore.themePreference

    override suspend fun setThemePreference(themePreference: ThemePreference) =
        dataStore.setThemePreference(themePreference)
}