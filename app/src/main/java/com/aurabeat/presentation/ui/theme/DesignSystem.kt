package com.aurabeat.presentation.ui.theme

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

object AuraSpacing {
    val xs = AppSpacing.xs
    val sm = AppSpacing.sm
    val md = AppSpacing.md
    val lg = AppSpacing.lg
    val xl = AppSpacing.xl
    val xxl = AppSpacing.xxl
}

object AuraRadius {
    val sm = AppRadius.sm
    val md = AppRadius.md
    val lg = AppRadius.lg
    val xl = AppRadius.xl
}

object AuraElevation {
    val card = AppElevation.card
    val prominent = AppElevation.prominent
}

@Composable
fun auraScreenGradient(): Brush {
    val colors = MaterialTheme.colorScheme
    val isDark = colors.background.luminance() < 0.5f
    return Brush.verticalGradient(
        if (isDark) {
            listOf(
                colors.surfaceVariant.copy(alpha = 0.72f),
                colors.background,
                colors.background
            )
        } else {
            listOf(
                colors.primaryContainer.copy(alpha = 0.36f),
                colors.background,
                colors.surfaceVariant.copy(alpha = 0.22f)
            )
        }
    )
}

@Composable
fun auraCardColors() = CardDefaults.cardColors(
    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
)
