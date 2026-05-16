package com.aurabeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.aurabeat.core.AppContainer
import com.aurabeat.presentation.theme.ThemeMode
import com.aurabeat.presentation.ui.navigation.AuraBeatNavHost
import com.aurabeat.presentation.ui.theme.AuraBeatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // rememberSaveable persists theme choice through activity recreation.
            // This can later move to DataStore inside a ThemeViewModel.
            var themeMode by rememberSaveable { mutableStateOf(ThemeMode.Dark) }
            val isDarkTheme = themeMode == ThemeMode.Dark
            val appContainer = AppContainer()

            AuraBeatTheme(darkTheme = isDarkTheme) {
                AuraBeatNavHost(
                    isDarkTheme = isDarkTheme,
                    appContainer = appContainer,
                    onToggleTheme = {
                        themeMode = if (isDarkTheme) ThemeMode.Light else ThemeMode.Dark
                    }
                )
            }
        }
    }
}
