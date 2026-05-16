package com.aurabeat.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aurabeat.presentation.ui.theme.AppSpacing

@Composable
fun MoodInputCard(
    moodText: String,
    onMoodTextChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg)
        ) {
            Text(
                text = "Describe your mood",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            AppTextField(
                value = moodText,
                onValueChange = onMoodTextChange,
                placeholder = "I feel nostalgic tonight",
                minLines = 3,
                supportingText = "Try: \"Give me energetic gym music\" or \"Relaxing study vibes\""
            )
            AppButton(
                text = "Generate Playlist",
                onClick = onGenerateClick,
                enabled = moodText.isNotBlank()
            )
        }
    }
}

