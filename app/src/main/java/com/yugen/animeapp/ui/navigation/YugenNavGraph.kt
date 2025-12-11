package com.yugen.animeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yugen.animeapp.ui.screen.animedetails.AnimeDetailsScreen
import com.yugen.animeapp.ui.screen.animelist.AnimeListScreen
import com.yugen.animeapp.ui.screen.favourite.FavouriteAnimeScreen
import com.yugen.animeapp.ui.screen.home.HomeScreen
import com.yugen.animeapp.ui.screen.onboarding.OnboardingScreen
import com.yugen.animeapp.ui.screen.profile.ProfileScreen
import com.yugen.animeapp.ui.screen.search.SearchScreen
import com.yugen.animeapp.ui.screen.splash.SplashScreen

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
                    navigateToAnimeDetails = navController::navigateToHomeAnimeDetails
                )
            }
            composable<Route.Search> {
                SearchScreen(navigateToAnimeDetails = navController::navigateToHomeAnimeDetails)
            }
            composable<Route.AnimeList> {
                AnimeListScreen(navigateToAnimeDetails = navController::navigateToHomeAnimeDetails)
            }
            composable<Route.HomeAnimeDetails> {
                AnimeDetailsScreen()
            }
        }
        navigation<Route.FavouriteGraph>(
            startDestination = Route.Favourite
        ) {
            composable<Route.Favourite> {
                FavouriteAnimeScreen(navigateToAnimeDetails = navController::navigateToFavouriteAnimeDetails)
            }
            composable<Route.FavouriteAnimeDetails> {
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

private fun NavHostController.navigateToHomeAnimeDetails(animeId: Int) =
    navigate((Route.HomeAnimeDetails(animeId)))

private fun NavHostController.navigateToFavouriteAnimeDetails(animeId: Int) =
    navigate((Route.FavouriteAnimeDetails(animeId)))