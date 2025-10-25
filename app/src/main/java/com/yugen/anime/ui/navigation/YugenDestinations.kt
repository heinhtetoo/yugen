package com.yugen.anime.ui.navigation

import com.yugen.anime.R

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

object HomeDestination : NavigationDestination {

    override val route = "home"
    override val titleRes = R.string.home_screen_title
}