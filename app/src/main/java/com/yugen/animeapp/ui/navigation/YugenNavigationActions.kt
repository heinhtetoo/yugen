package com.yugen.animeapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalLibrary
import androidx.compose.material.icons.rounded.Person2
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.yugen.animeapp.R

data class YugenTopLevelDestination(
    val route: Route,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

class YugenNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: YugenTopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    YugenTopLevelDestination(
        route = Route.HomeGraph,
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.home_screen_title
    ),
    YugenTopLevelDestination(
        route = Route.LibraryGraph,
        selectedIcon = Icons.Rounded.LocalLibrary,
        unselectedIcon = Icons.Outlined.LocalLibrary,
        iconTextId = R.string.library_screen_title
    ),
    YugenTopLevelDestination(
        route = Route.Profile,
        selectedIcon = Icons.Rounded.Person2,
        unselectedIcon = Icons.Outlined.Person2,
        iconTextId = R.string.profile_screen_title
    )
)