package com.aurabeat.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.aurabeat.presentation.ui.model.MoodAnalysis
import com.aurabeat.presentation.ui.theme.AppSpacing

@Composable
fun MoodAnalysisCard(
    analysis: MoodAnalysis,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
        Column(
            modifier = Modifier.padding(AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Text(
                text = "AI Mood Analysis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                AnalysisChip(label = "Mood", value = analysis.mood)
                AnalysisChip(label = "Energy", value = analysis.energy)
            }

            AnalysisChip(label = "Vibe", value = analysis.vibe)

            Text(
                text = "Genres",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                analysis.genres.forEach { genre ->
                    AppChip(text = genre, selected = true, onClick = {})
                }
            }

            Text(
                text = "Emotional Keywords",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                analysis.keywords.forEach { keyword ->
                    AppChip(text = keyword, selected = false, onClick = {})
                }
            }
        }
    }
}

@Composable
private fun AnalysisChip(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}
