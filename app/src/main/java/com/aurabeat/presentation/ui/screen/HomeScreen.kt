package com.aurabeat.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurabeat.presentation.ui.component.AppChip
import com.aurabeat.presentation.ui.component.ErrorState
import com.aurabeat.presentation.ui.component.MoodInputCard
import com.aurabeat.presentation.ui.component.PlaylistCard
import com.aurabeat.presentation.ui.component.SectionTitle
import com.aurabeat.presentation.ui.model.MoodDiscoveryMockData
import com.aurabeat.presentation.ui.theme.AppLayout
import com.aurabeat.presentation.ui.theme.AppSpacing
import com.aurabeat.presentation.ui.theme.auraScreenGradient
import com.aurabeat.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    contentPadding: PaddingValues = WindowInsets.statusBars.asPaddingValues(),
    homeViewModel: HomeViewModel = remember { HomeViewModel() },
    onArtistClick: (String) -> Unit = {},
    onGenerateClick: (String) -> Unit = {}
) {
    // Local text input is still kept for editability, but featured playlists come from ViewModel.
    var moodText by rememberSaveable { mutableStateOf("") }
    val state by homeViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(auraScreenGradient())
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    horizontal = AppLayout.screenHorizontalPadding,
                    vertical = AppLayout.screenVerticalPadding
                ),
                verticalArrangement = Arrangement.spacedBy(AppLayout.sectionSpacing)
            ) {
                item {
                    HeaderSection()
                }

                item {
                    MoodInputCard(
                        moodText = moodText,
                        onMoodTextChange = { moodText = it },
                        onGenerateClick = {
                            onGenerateClick(moodText)
                        }
                    )
                }

                if (state.isLoading) {
                    item {
                        Text(
                            text = "Loading trending playlists...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                state.error?.let { error ->
                    item {
                        ErrorState(
                            message = error,
                            onRetry = homeViewModel::loadTrendingPlaylists
                        )
                    }
                }

                item {
                    SectionTitle(title = "Quick Moods")
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                        items(MoodDiscoveryMockData.quickMoods) { mood ->
                            val selected = moodText == mood.presetPrompt
                            AppChip(
                                text = mood.label,
                                selected = selected,
                                onClick = {
                                    moodText = mood.presetPrompt
                                    // Future API-ready path: dispatch selected mood preset to ViewModel.
                                }
                            )
                        }
                    }
                }

                item {
                    SectionTitle(title = "Trending Playlists")
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                        items(state.trendingPlaylists) { playlist ->
                            PlaylistCard(
                                playlist = playlist,
                                modifier = Modifier
                                    .fillParentMaxWidth(0.68f),
                                onClick = { onArtistClick("the-weeknd") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Text(
                text = "AuraBeat",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "Welcome back",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Find the perfect soundtrack for your mood",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(0.92f)
        )

        Box(modifier = Modifier.height(4.dp))
    }
}

