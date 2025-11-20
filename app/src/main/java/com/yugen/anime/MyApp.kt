package com.yugen.anime

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.yugen.anime.core.ui.theme.YugenTheme
import com.yugen.anime.domain.model.ThemePreference
import com.yugen.anime.ui.component.YugenBottomNavigationBar
import com.yugen.anime.ui.component.currentDestination
import com.yugen.anime.ui.navigation.YugenNavHost
import com.yugen.anime.ui.navigation.YugenNavigationActions
import com.yugen.anime.ui.screen.profile.ProfileViewModel

@Composable
fun MyApp(profileViewModel: ProfileViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val navActions = remember(navController) { YugenNavigationActions(navController) }

    val theme = profileViewModel.theme.collectAsState()

    val darkTheme = when (theme.value) {
        ThemePreference.DARK -> true
        ThemePreference.LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    YugenTheme(darkTheme = darkTheme) {
        Scaffold(
            bottomBar = {
                YugenBottomNavigationBar(
                    currentDestination = currentDestination(navController),
                    navigateToTopLevelDestination = navActions::navigateTo
                )
            }
        ) { innerPadding ->
            YugenNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}