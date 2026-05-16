package com.aurabeat.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(AppRadius.sm),
    small = RoundedCornerShape(AppRadius.md),
    medium = RoundedCornerShape(AppRadius.lg),
    large = RoundedCornerShape(AppRadius.xl),
    extraLarge = RoundedCornerShape(AppRadius.xxl)
)
