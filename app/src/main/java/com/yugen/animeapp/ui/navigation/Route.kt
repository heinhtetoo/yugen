package com.yugen.animeapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Splash : Route
    @Serializable
    data object Onboarding : Route
    @Serializable
    data object MainApp : Route
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