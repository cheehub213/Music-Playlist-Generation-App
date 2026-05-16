package com.aurabeat.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.aurabeat.presentation.ui.component.AppButton
import com.aurabeat.presentation.ui.component.GeneratedSongRow
import com.aurabeat.presentation.ui.component.MoodAnalysisCard
import com.aurabeat.presentation.ui.component.PlaylistHeader
import com.aurabeat.presentation.ui.model.MoodTrack
import com.aurabeat.presentation.ui.theme.AppLayout
import com.aurabeat.presentation.ui.theme.AppSpacing
import com.aurabeat.presentation.ui.theme.auraScreenGradient
import com.aurabeat.presentation.viewmodel.MoodViewModel
import com.aurabeat.presentation.viewmodel.MockSong
import com.aurabeat.presentation.viewmodel.PlayerViewModel

@Composable
fun GeneratedPlaylistScreen(
    moodViewModel: MoodViewModel,
    playerViewModel: PlayerViewModel,
    contentPadding: PaddingValues = WindowInsets.statusBars.asPaddingValues(),
    onSongClick: (String) -> Unit
) {
    val state by moodViewModel.uiState.collectAsState()
    val playlist = state.playlist
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            snackbarHostState.showSnackbar("Added to Library")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(auraScreenGradient())
            .padding(contentPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = AppLayout.screenHorizontalPadding,
                    vertical = AppLayout.screenVerticalPadding
                ),
            verticalArrangement = Arrangement.spacedBy(AppLayout.sectionSpacing)
        ) {
            item {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    PlaylistHeader(
                        playlist = playlist,
                        isSaved = state.isSaved,
                        onPlayClick = {
                            playlist.tracks.firstOrNull()?.let {
                                playTrack(it, playlist.tracks, playerViewModel, onSongClick)
                            }
                        },
                        onShuffleClick = {
                            playlist.tracks.randomOrNull()?.let {
                                playTrack(it, playlist.tracks, playerViewModel, onSongClick)
                            }
                        },
                        onSaveToggle = moodViewModel::toggleSaved
                    )
                }
            }

            item {
                AppButton(
                    text = "Regenerate Playlist",
                    onClick = moodViewModel::regenerate
                )
            }

            item {
                MoodAnalysisCard(analysis = playlist.analysis)
            }

            item {
                Text(
                    text = "Generated Songs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(playlist.tracks, key = { it.id }) { track ->
                GeneratedSongRow(
                    track = track,
                    onPlayClick = { selected ->
                        playTrack(selected, playlist.tracks, playerViewModel, onSongClick)
                    },
                    onRowClick = { selected ->
                        playTrack(selected, playlist.tracks, playerViewModel, onSongClick)
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = AppSpacing.xl, vertical = AppSpacing.lg)
        )
    }
}

private fun playTrack(
    track: MoodTrack,
    tracks: List<MoodTrack>,
    playerViewModel: PlayerViewModel,
    onSongClick: (String) -> Unit
) {
    val queue = tracks.map { it.toMockSong() }
    playerViewModel.playCustomQueue(queue, track.id)
    onSongClick(track.id)
}

private fun MoodTrack.toMockSong(): MockSong {
    return MockSong(
        id = id,
        title = title,
        artist = artist,
        album = "AuraBeat Generated",
        durationSeconds = durationSeconds,
        artworkColor = artworkColor
    )
}
