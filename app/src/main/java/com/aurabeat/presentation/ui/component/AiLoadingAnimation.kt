package com.aurabeat.presentation.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.aurabeat.presentation.ui.theme.AppColors

@Composable
fun AiLoadingAnimation(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "ai_loading")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ai_rotation"
    )
    val pulse by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ai_pulse"
    )
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val surfaceHigh = MaterialTheme.colorScheme.surfaceContainerHigh

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(170.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AppColors.Brand.copy(alpha = 0.48f),
                            Color.Transparent
                        )
                    )
                )
                .blur(24.dp)
        )

        Box(
            modifier = Modifier
                .size(132.dp)
                .clip(CircleShape)
                .background(surfaceHigh.copy(alpha = 0.85f))
                .rotate(rotation)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    brush = Brush.sweepGradient(
                        listOf(
                            primary,
                            secondary,
                            primary
                        )
                    ),
                    style = Stroke(width = 10.dp.toPx())
                )
            }
        }

        Canvas(
            modifier = Modifier
                .size((92.dp * pulse))
        ) {
            val barWidth = size.width / 11f
            val barMaxHeight = size.height / 2.4f
            val centerY = size.height / 2f
            for (i in 0 until 9) {
                val heightFactor = 0.35f + (kotlin.math.sin((i + rotation / 40f)) + 1f) * 0.18f
                val barHeight = barMaxHeight * heightFactor
                val x = (i + 1) * barWidth
                drawRoundRect(
                    color = primary,
                    topLeft = Offset(x, centerY - barHeight / 2f),
                    size = androidx.compose.ui.geometry.Size(barWidth * 0.6f, barHeight),
                    cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                )
            }
        }
    }
}
