package com.yugen.animeapp.ui.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yugen.animeapp.ui.navigation.TOP_LEVEL_DESTINATIONS
import com.yugen.animeapp.ui.navigation.YugenTopLevelDestination

@Composable
fun YugenBottomNavigationBar(
    currentDestination: NavDestination?,
    navigateToTopLevelDestination: (YugenTopLevelDestination) -> Unit
) {
    Log.e("ROUTE", currentDestination.debugPath())
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { topLevelDestination ->
            NavigationBarItem(
                selected = currentDestination.hasRoute(topLevelDestination),
                onClick = { navigateToTopLevelDestination(topLevelDestination) },
                icon = {
                    Icon(
                        imageVector = if (currentDestination.hasRoute(topLevelDestination)) topLevelDestination.selectedIcon else topLevelDestination.unselectedIcon,
                        contentDescription = stringResource(topLevelDestination.iconTextId)
                    )
                },
                label = {
                    if (currentDestination.hasRoute(topLevelDestination)) Text(
                        stringResource(
                            topLevelDestination.iconTextId
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun currentDestination(navController: NavHostController): NavDestination? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination
}

fun NavDestination?.hasRoute(topLevelDestination: YugenTopLevelDestination): Boolean {
//    return this?.hierarchy?.any { it.route == topLevelDestination.route::class.qualifiedName } == true
    return this?.hierarchy?.any { it.hasRoute(topLevelDestination.route::class) } == true
}

@SuppressLint("RestrictedApi")
fun NavDestination?.debugPath(): String {
    return this?.hierarchy
        ?.joinToString(" -> ") { it.route ?: it.displayName }
        ?: "No destination"
}
