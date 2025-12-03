package com.yugen.anime.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import com.yugen.anime.R
import com.yugen.anime.ui.screen.animedetails.AnimeDetailsScreen
import com.yugen.anime.ui.screen.animelist.AnimeListScreen
import com.yugen.anime.ui.screen.favourite.FavouriteAnimeScreen
import com.yugen.anime.ui.screen.home.HomeScreen
import com.yugen.anime.ui.screen.profile.ProfileScreen
import com.yugen.anime.ui.screen.search.SearchScreen
import kotlinx.coroutines.delay

@Composable
fun YugenNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash,
        modifier = modifier
    ) {
        composable<Route.Splash> {
            LaunchedEffect(Unit) {
                delay(2_000)
                navController.navigate(Route.Personalisation)
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.size(64.dp)
                )
            }
        }
        composable<Route.Personalisation> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { navController.navigate(Route.HomeGraph) }) {
                    Text(text = "Start")
                }
            }
        }
        navigation<Route.HomeGraph>(
            startDestination = Route.Home
        ) {
            composable<Route.Home> {
                HomeScreen(
                    navigateToSearch = navController::navigateToSearch,
                    navigateToAnimeList = navController::navigateToAnimeList,
                    navigateToAnimeDetails = navController::navigateToAnimeDetails
                )
            }
            composable<Route.Search> {
                SearchScreen(navigateToAnimeDetails = navController::navigateToAnimeDetails)
            }
            composable<Route.AnimeList> {
                AnimeListScreen(navigateToAnimeDetails = navController::navigateToAnimeDetails)
            }
            composable<Route.AnimeDetails> {
                AnimeDetailsScreen()
            }
        }
        navigation<Route.FavouriteGraph>(
            startDestination = Route.Favourite
        ) {
            composable<Route.Favourite> {
                FavouriteAnimeScreen(navigateToAnimeDetails = navController::navigateToAnimeDetails)
            }
            composable<Route.AnimeDetails> {
                AnimeDetailsScreen()
            }
        }
        composable<Route.Profile> {
            ProfileScreen()
        }
    }
}

private fun NavHostController.navigateToSearch() =
    navigate((Route.Search))

private fun NavHostController.navigateToAnimeList(genreId: Int) =
    navigate((Route.AnimeList(genreId)))

private fun NavHostController.navigateToAnimeDetails(animeId: Int) =
    navigate((Route.AnimeDetails(animeId)))