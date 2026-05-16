package com.aurabeat.presentation.ui.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext

val DarkColorScheme = darkColorScheme(
    primary = AuraGreen,
    onPrimary = AuraInk,
    primaryContainer = AuraGreenDark,
    onPrimaryContainer = AuraWhite,
    secondary = AuraDarkTextMuted,
    onSecondary = AuraInk,
    background = AuraDarkBackground,
    onBackground = AuraDarkText,
    surface = AuraDarkSurface,
    onSurface = AuraDarkText,
    surfaceVariant = AuraDarkSurfaceHigh,
    onSurfaceVariant = AuraDarkTextMuted,
    surfaceContainer = AuraDarkSurface,
    surfaceContainerHigh = AuraDarkSurfaceHigh,
    outline = AuraDarkOutline,
    error = AuraError
)

val LightColorScheme = lightColorScheme(
    primary = AuraGreenDark,
    onPrimary = AuraWhite,
    primaryContainer = ColorPalette.LightGreenContainer,
    onPrimaryContainer = AuraInk,
    secondary = ColorPalette.LightSecondary,
    onSecondary = AuraWhite,
    background = AuraLightBackground,
    onBackground = AuraLightText,
    surface = AuraLightSurface,
    onSurface = AuraLightText,
    surfaceVariant = AuraLightSurfaceHigh,
    onSurfaceVariant = AuraLightTextMuted,
    surfaceContainer = AuraLightSurface,
    surfaceContainerHigh = AuraLightSurfaceHigh,
    outline = AuraLightOutline,
    error = ColorPalette.LightError
)

@Composable
fun AuraBeatTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val baseColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && darkTheme -> {
            dynamicDarkColorScheme(context)
        }
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val background by animateColorAsState(baseColorScheme.background, label = "theme_background")
    val surface by animateColorAsState(baseColorScheme.surface, label = "theme_surface")
    val surfaceVariant by animateColorAsState(baseColorScheme.surfaceVariant, label = "theme_surface_variant")
    val primary by animateColorAsState(baseColorScheme.primary, label = "theme_primary")

    MaterialTheme(
        colorScheme = baseColorScheme.copy(
            background = background,
            surface = surface,
            surfaceVariant = surfaceVariant,
            primary = primary
        ),
        typography = AuraBeatTypography,
        shapes = AppShapes,
        content = content
    )
}

private object ColorPalette {
    val LightGreenContainer = androidx.compose.ui.graphics.Color(0xFFDDF8E7)
    val LightSecondary = androidx.compose.ui.graphics.Color(0xFF42534A)
    val LightError = androidx.compose.ui.graphics.Color(0xFFBA1A1A)
}
