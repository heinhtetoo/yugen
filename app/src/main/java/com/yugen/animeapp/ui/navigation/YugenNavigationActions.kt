package com.yugen.animeapp.ui.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.yugen.animeapp.R
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Splash : Route
    @Serializable
    data object Onboarding : Route
    @Serializable
    data object HomeGraph : Route
    @Serializable
    data object Home : Route
    @Serializable
    data object Search : Route
    @Serializable
    data class AnimeList(val genreId: Int) : Route
    @Serializable
    data class HomeAnimeDetails(val animeId: Int) : Route
    @Serializable
    data object FavouriteGraph : Route
    @Serializable
    data object Favourite : Route
    @Serializable
    data class FavouriteAnimeDetails(val animeId: Int) : Route
    @Serializable
    data object Profile : Route
}

data class YugenTopLevelDestination(
    val route: Route,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

class YugenNavigationActions(private val navController: NavHostController) {

    // TODO :: persist last destination??
    fun navigateTo(destination: YugenTopLevelDestination) {
        navController.navigate(destination.route) {
//            popUpTo<Route.HomeGraph> { saveState = true }
//            popUpTo(navController.graph.findStartDestination().id) { saveState = true }

            val homeGraphId = navController.graph.findNode<Route.HomeGraph>()?.id

            if (homeGraphId != null) {
                popUpTo(homeGraphId) {
                    saveState = true
                }
            } else {
                // Fallback (Should not happen if graph is set up right)
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
            }

            launchSingleTop = true
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    YugenTopLevelDestination(
        route = Route.HomeGraph,
        selectedIcon = Icons.Filled.LocalLibrary,
        unselectedIcon = Icons.Outlined.LocalLibrary,
        iconTextId = R.string.home_screen_title
    ),
    YugenTopLevelDestination(
        route = Route.FavouriteGraph,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        iconTextId = R.string.favourite_screen_title
    ),
    YugenTopLevelDestination(
        route = Route.Profile,
        selectedIcon = Icons.Filled.Person2,
        unselectedIcon = Icons.Outlined.Person2,
        iconTextId = R.string.profile_screen_title
    )
)