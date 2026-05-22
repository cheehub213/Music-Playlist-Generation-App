package com.aurabeat.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.aurabeat.presentation.ui.component.AiLoadingAnimation
import com.aurabeat.presentation.ui.theme.AppLayout
import com.aurabeat.presentation.ui.theme.AppSpacing
import com.aurabeat.presentation.ui.theme.auraScreenGradient
import com.aurabeat.presentation.viewmodel.MoodViewModel
import kotlinx.coroutines.delay

@Composable
fun MoodLoadingScreen(
    moodViewModel: MoodViewModel,
    contentPadding: PaddingValues = WindowInsets.statusBars.asPaddingValues(),
    onFinished: () -> Unit,
    onBack: () -> Unit = {}
) {
    val state by moodViewModel.uiState.collectAsState()

    LaunchedEffect(state.isGenerating, state.error) {
        if (!state.isGenerating && state.error == null) {
            onFinished()
            return@LaunchedEffect
        }
        
        if (state.isGenerating) {
            // Animate while API processes (up to 15 seconds total)
            val steps = 6
            val delayPerStep = 2500L  // 2.5 sec per step = 15 sec total
            for (i in 0 until steps) {
                if (!state.isGenerating) break
                moodViewModel.updateLoading(i % 4, (i % 4 + 1) / 4f)
                delay(delayPerStep)
            }
            // Max time reached, force completion
            if (state.isGenerating) {
                moodViewModel.completeGeneration()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(auraScreenGradient())
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = AppLayout.screenHorizontalPadding,
                    vertical = AppLayout.screenVerticalPadding
                ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "AuraBeat AI",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            if (state.error != null) {
                Text(
                    text = "Oops! Something went wrong",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                
                Text(
                    text = state.error ?: "Unknown error",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                
                Button(
                    onClick = {
                        moodViewModel.clearError()
                        onBack()
                    },
                    modifier = Modifier.padding(top = AppSpacing.lg)
                ) {
                    Text("Go Back")
                }
            } else {
                Text(
                    text = "Generating your playlist",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                AiLoadingAnimation()

                AnimatedVisibility(
                    visible = state.isGenerating,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = state.loadingMessage,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LinearProgressIndicator(
                    progress = { state.loadingProgress },
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .padding(top = AppSpacing.sm),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Text(
                    text = "Prompt: ${state.prompt}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
