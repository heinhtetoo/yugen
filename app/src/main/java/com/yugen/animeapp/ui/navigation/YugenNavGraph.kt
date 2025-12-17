package com.yugen.animeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yugen.animeapp.MainAppScreen
import com.yugen.animeapp.ui.screen.animedetails.AnimeDetailsScreen
import com.yugen.animeapp.ui.screen.animelist.AnimeListScreen
import com.yugen.animeapp.ui.screen.favourite.FavouriteAnimeScreen
import com.yugen.animeapp.ui.screen.home.HomeScreen
import com.yugen.animeapp.ui.screen.onboarding.OnboardingScreen
import com.yugen.animeapp.ui.screen.profile.ProfileScreen
import com.yugen.animeapp.ui.screen.search.SearchScreen
import com.yugen.animeapp.ui.screen.splash.SplashScreen

@Composable
fun YugenRootNavHost(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = rootNavController,
        startDestination = Route.Splash,
        modifier = modifier
    ) {
        composable<Route.Splash> {
            SplashScreen(
                navigateToOnboarding = rootNavController::navigateToOnboarding,
                navigateToHome = { rootNavController.navigateToMainApp(Route.Splash) }
            )
        }
        composable<Route.Onboarding> {
            OnboardingScreen(
                navigateToHome = { rootNavController.navigateToMainApp(Route.Onboarding) })
        }
        composable<Route.MainApp> {
            MainAppScreen()
        }
    }
}

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
                AnimeDetailsScreen(
                    navigateBack = navController::navigateUp,
                    navigateToAnimeDetails = navController::navigateToHomeAnimeDetails
                )
            }
        }
        navigation<Route.FavouriteGraph>(
            startDestination = Route.Favourite
        ) {
            composable<Route.Favourite> {
                FavouriteAnimeScreen(navigateToAnimeDetails = navController::navigateToFavouriteAnimeDetails)
            }
            composable<Route.FavouriteAnimeDetails> {
                AnimeDetailsScreen(
                    navigateBack = navController::navigateUp,
                    navigateToAnimeDetails = navController::navigateToFavouriteAnimeDetails
                )
            }
        }
        composable<Route.Profile> {
            ProfileScreen()
        }
    }
}

private fun NavHostController.navigateToOnboarding() =
    navigate(Route.Onboarding) { popUpTo<Route.Splash> { inclusive = true } }

private fun NavHostController.navigateToMainApp(route: Route) =
    when (route) {
        is Route.Splash -> {
            navigate(Route.MainApp) { popUpTo<Route.Splash> { inclusive = true } }
        }

        is Route.Onboarding -> {
            navigate(Route.MainApp) { popUpTo<Route.Onboarding> { inclusive = true } }
        }

        else -> null
    }

private fun NavHostController.navigateToSearch() =
    navigate((Route.Search))

private fun NavHostController.navigateToAnimeList(genreId: Int) =
    navigate((Route.AnimeList(genreId)))

private fun NavHostController.navigateToHomeAnimeDetails(animeId: Int) =
    navigate((Route.HomeAnimeDetails(animeId)))

private fun NavHostController.navigateToFavouriteAnimeDetails(animeId: Int) =
    navigate((Route.FavouriteAnimeDetails(animeId)))