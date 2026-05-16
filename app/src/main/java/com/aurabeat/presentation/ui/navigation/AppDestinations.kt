package com.aurabeat.presentation.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AppDestination(
    val route: String,
    val label: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<String> = emptyList()
) {
    data object Home : AppDestination("home", "Home")
    data object Search : AppDestination("search", "Search")
    data object Library : AppDestination("library", "Library")
    data object Profile : AppDestination("profile", "Profile")

    data object PlaylistDetails : AppDestination(
        route = "playlist/{playlistId}",
        label = "Playlist",
        arguments = listOf(navArgument("playlistId") { type = NavType.StringType }),
        deepLinks = listOf("aurabeat://playlist/{playlistId}")
    ) {
        fun createRoute(playlistId: String) = "playlist/$playlistId"
    }

    data object ArtistDetails : AppDestination(
        route = "artist/{artistId}",
        label = "Artist",
        arguments = listOf(navArgument("artistId") { type = NavType.StringType }),
        deepLinks = listOf("aurabeat://artist/{artistId}")
    ) {
        fun createRoute(artistId: String) = "artist/$artistId"
    }

    data object Player : AppDestination(
        route = "player/{songId}",
        label = "Player",
        arguments = listOf(navArgument("songId") { type = NavType.StringType }),
        deepLinks = listOf("aurabeat://player/{songId}")
    ) {
        fun createRoute(songId: String) = "player/$songId"
    }

    data object MoodLoading : AppDestination("mood_loading", "AI Loading")
    data object Settings : AppDestination("settings", "Settings")
    data object GeneratedPlaylist : AppDestination("generated_playlist", "Generated Playlist")
}

val bottomLevelDestinations = listOf(
    AppDestination.Home,
    AppDestination.Search,
    AppDestination.Library,
    AppDestination.Profile
)
