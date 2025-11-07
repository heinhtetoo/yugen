package com.yugen.anime.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.yugen.anime.R
import com.yugen.anime.ui.screen.animedetails.AnimeDetailsScreen
import com.yugen.anime.ui.screen.home.HomeScreen

@Composable
fun YugenNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.HomeGraph,
        modifier = modifier
    ) {
        navigation<Route.HomeGraph>(
            startDestination = Route.Home
        ) {
            composable<Route.Home> {
                HomeScreen(navigateToAnimeDetails = { animeId ->
                    navController.navigate(Route.AnimeDetails(animeId = animeId))
                })
            }
            composable<Route.AnimeDetails> {
                AnimeDetailsScreen()
            }
        }
        composable<Route.Favourite> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.favourite_screen_title))
            }
        }
        composable<Route.Profile> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.profile_screen_title))
            }
        }
    }
}