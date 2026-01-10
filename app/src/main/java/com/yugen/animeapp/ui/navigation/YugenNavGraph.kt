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
import com.yugen.animeapp.ui.screen.library.LibraryScreen
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
                SearchScreen(
                    navigateBack = navController::navigateUp,
                    navigateToAnimeDetails = navController::navigateToHomeAnimeDetails
                )
            }
            composable<Route.AnimeList> {
                AnimeListScreen(
                    navigateBack = navController::navigateUp,
                    navigateToAnimeDetails = navController::navigateToHomeAnimeDetails
                )
            }
            composable<Route.HomeAnimeDetails> {
                AnimeDetailsScreen(
                    navigateBack = navController::navigateUp,
                    navigateToAnimeDetails = navController::navigateToHomeAnimeDetails
                )
            }
        }
        navigation<Route.LibraryGraph>(
            startDestination = Route.Library
        ) {
            composable<Route.Library> {
                LibraryScreen(navigateToAnimeDetails = navController::navigateToLibraryAnimeDetails)
            }
            composable<Route.LibraryAnimeDetails> {
                AnimeDetailsScreen(
                    navigateBack = navController::navigateUp,
                    navigateToAnimeDetails = navController::navigateToLibraryAnimeDetails
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

private fun NavHostController.navigateToAnimeList(genreId: Int, title: String) =
    navigate((Route.AnimeList(genreId, title)))

private fun NavHostController.navigateToHomeAnimeDetails(animeId: Int) =
    navigate((Route.HomeAnimeDetails(animeId)))

private fun NavHostController.navigateToLibraryAnimeDetails(animeId: Int) =
    navigate((Route.LibraryAnimeDetails(animeId)))