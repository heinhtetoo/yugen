package com.yugen.anime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yugen.anime.ui.screen.animedetails.AnimeDetailsScreen
import com.yugen.anime.ui.screen.animelist.AnimeListScreen
import com.yugen.anime.ui.screen.favourite.FavouriteAnimeScreen
import com.yugen.anime.ui.screen.home.HomeScreen
import com.yugen.anime.ui.screen.onboarding.OnboardingScreen
import com.yugen.anime.ui.screen.profile.ProfileScreen
import com.yugen.anime.ui.screen.search.SearchScreen
import com.yugen.anime.ui.screen.splash.SplashScreen

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
            SplashScreen(
                navigateToOnboarding = navController::navigateToOnboarding,
                navigateToHome = { navController.navigateToHomeGraph(Route.Splash) }
            )
        }
        composable<Route.Onboarding> {
            OnboardingScreen(
                navigateToHome = { navController.navigateToHomeGraph(Route.Onboarding) })
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

private fun NavHostController.navigateToOnboarding() =
    navigate(Route.Onboarding) { popUpTo<Route.Splash> { inclusive = true } }

private fun NavHostController.navigateToHomeGraph(route: Route) =
    when (route) {
        is Route.Splash -> {
            navigate(Route.HomeGraph) { popUpTo<Route.Splash> { inclusive = true } }
        }

        is Route.Onboarding -> {
            navigate(Route.HomeGraph) { popUpTo<Route.Onboarding> { inclusive = true } }
        }

        else -> {
            navigate(Route.HomeGraph)
        }
    }

private fun NavHostController.navigateToSearch() =
    navigate((Route.Search))

private fun NavHostController.navigateToAnimeList(genreId: Int) =
    navigate((Route.AnimeList(genreId)))

private fun NavHostController.navigateToAnimeDetails(animeId: Int) =
    navigate((Route.AnimeDetails(animeId)))