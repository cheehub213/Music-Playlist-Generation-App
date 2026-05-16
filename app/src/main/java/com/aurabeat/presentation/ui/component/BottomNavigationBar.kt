package com.aurabeat.presentation.ui.component

import androidx.compose.runtime.Composable
import com.aurabeat.presentation.ui.navigation.AppDestination

@Composable
fun AuraBeatBottomNavigationBar(
    currentRoute: String?,
    onNavigate: (AppDestination) -> Unit
) {
    BottomNavBar(
        currentRoute = currentRoute,
        onNavigate = onNavigate
    )
}
