package com.yugen.anime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.yugen.anime.domain.model.AnimeCategory
import com.yugen.anime.ui.screen.animedetails.AnimeDetailsScreen
import com.yugen.anime.ui.screen.animelist.AnimeListScreen
import com.yugen.anime.ui.screen.favourite.FavouriteAnimeScreen
import com.yugen.anime.ui.screen.home.HomeScreen
import com.yugen.anime.ui.screen.profile.ProfileScreen
import com.yugen.anime.ui.screen.search.SearchScreen

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

private fun NavHostController.navigateToAnimeList(animeCategory: AnimeCategory) =
    navigate((Route.AnimeList(animeCategory)))

private fun NavHostController.navigateToAnimeDetails(animeId: Int, animeCategory: AnimeCategory) =
    navigate((Route.AnimeDetails(animeId, animeCategory)))