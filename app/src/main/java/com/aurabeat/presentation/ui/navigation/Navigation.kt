package com.aurabeat.presentation.ui.navigation

import androidx.compose.runtime.Composable
import com.aurabeat.core.AppContainer

@Composable
fun AuraBeatNavHost(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    appContainer: AppContainer = AppContainer()
) {
    AuraBeatNavigationGraph(
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        appContainer = appContainer
    )
}
