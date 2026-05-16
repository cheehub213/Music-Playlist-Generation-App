package com.aurabeat.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val destination: AppDestination,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector = selectedIcon
)

val bottomNavItems = listOf(
    BottomNavItem(AppDestination.Home, Icons.Rounded.Home),
    BottomNavItem(AppDestination.Search, Icons.Rounded.Search),
    BottomNavItem(AppDestination.Library, Icons.Rounded.LibraryMusic),
    BottomNavItem(AppDestination.Profile, Icons.Rounded.AccountCircle)
)
