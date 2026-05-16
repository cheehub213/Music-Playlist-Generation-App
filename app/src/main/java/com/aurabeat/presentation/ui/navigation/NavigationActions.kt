package com.aurabeat.presentation.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class NavigationActions(private val navController: NavHostController) {
    fun navigateToHome() = navigateToBottomDestination(AppDestination.Home)

    fun navigateToSearch() = navigateToBottomDestination(AppDestination.Search)

    fun navigateToLibrary() = navigateToBottomDestination(AppDestination.Library)

    fun navigateToProfile() = navigateToBottomDestination(AppDestination.Profile)

    fun navigateToArtist(artistId: String) {
        navController.navigateSingleTop(AppDestination.ArtistDetails.createRoute(artistId))
    }

    fun navigateToPlaylist(playlistId: String) {
        navController.navigateSingleTop(AppDestination.PlaylistDetails.createRoute(playlistId))
    }

    fun navigateToPlayer(songId: String) {
        navController.navigateSingleTop(AppDestination.Player.createRoute(songId))
    }

    fun navigateToMoodLoading() {
        navController.navigateSingleTop(AppDestination.MoodLoading.route)
    }

    fun navigateToGeneratedPlaylist() {
        navController.navigateSingleTop(AppDestination.GeneratedPlaylist.route)
    }

    fun navigateToSettings() {
        navController.navigateSingleTop(AppDestination.Settings.route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToBottomDestination(destination: AppDestination) {
        if (destination !in bottomLevelDestinations) return

        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

private fun NavHostController.navigateSingleTop(route: String) {
    if (currentDestination?.route == route) return

    navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}
