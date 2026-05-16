package com.aurabeat.presentation.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.aurabeat.presentation.ui.component.LyricsPreview
import com.aurabeat.presentation.ui.component.PlaybackControls
import com.aurabeat.presentation.ui.component.PlayerProgressBar
import com.aurabeat.presentation.ui.component.QueueBottomSheet
import com.aurabeat.presentation.ui.theme.AppColors
import com.aurabeat.presentation.ui.theme.AppIconSize
import com.aurabeat.presentation.ui.theme.AppLayout
import com.aurabeat.presentation.ui.theme.AppRadius
import com.aurabeat.presentation.ui.theme.AppSpacing
import com.aurabeat.presentation.ui.theme.auraScreenGradient
import com.aurabeat.presentation.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    songId: String,
    playerViewModel: PlayerViewModel,
    contentPadding: PaddingValues = WindowInsets.statusBars.asPaddingValues(),
    onMinimize: () -> Unit,
    onArtistClick: (String) -> Unit,
    onAlbumClick: (String) -> Unit
) {
    LaunchedEffect(songId) {
        playerViewModel.playSong(songId)
    }

    val state by playerViewModel.uiState.collectAsState()
    val song = state.currentSong ?: return
    var showQueue by remember { mutableStateOf(false) }

    LaunchedEffect(state.isPlaying, song.id) {
        while (state.isPlaying) {
            delay(1000)
            playerViewModel.tickProgress()
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
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = AppLayout.screenHorizontalPadding,
                    vertical = AppLayout.screenVerticalPadding
                ),
            verticalArrangement = Arrangement.spacedBy(AppLayout.sectionSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerTopBar(
                onMinimize = onMinimize,
                onQueueClick = { showQueue = true }
            )

            AlbumArtwork(
                color = Color(song.artworkColor),
                isPlaying = state.isPlaying,
                onClick = { onAlbumClick(song.album) }
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onArtistClick(song.artist) }
                )
                Text(
                    text = song.album,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            PlayerProgressBar(
                progressSeconds = state.progressSeconds,
                durationSeconds = song.durationSeconds,
                onSeek = playerViewModel::seekTo
            )

            PlaybackControls(
                isPlaying = state.isPlaying,
                isShuffleEnabled = state.isShuffleEnabled,
                isRepeatEnabled = state.isRepeatEnabled,
                onPlayPause = playerViewModel::togglePlayPause,
                onPrevious = playerViewModel::playPrevious,
                onNext = playerViewModel::playNext,
                onShuffle = playerViewModel::toggleShuffle,
                onRepeat = playerViewModel::toggleRepeat
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = playerViewModel::toggleFavorite) {
                    Icon(
                        imageVector = if (state.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (state.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { showQueue = true }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.QueueMusic,
                        contentDescription = "Queue",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            LyricsPreview()
        }
    }

    if (showQueue) {
        QueueBottomSheet(
            queue = state.queue,
            currentSongId = song.id,
            onDismiss = { showQueue = false },
            onSongClick = {
                playerViewModel.playSong(it)
                showQueue = false
            }
        )
    }
}

@Composable
private fun PlayerTopBar(
    onMinimize: () -> Unit,
    onQueueClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onMinimize) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Minimize player")
        }
        Text(
            text = "Now Playing",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        IconButton(onClick = onQueueClick) {
            Icon(Icons.AutoMirrored.Rounded.QueueMusic, contentDescription = "Queue")
        }
    }
}

@Composable
private fun AlbumArtwork(
    color: Color,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "artwork_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isPlaying) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "album_rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.xxl)
            .background(
                Brush.radialGradient(
                    listOf(color.copy(alpha = 0.38f), Color.Transparent)
                ),
                RoundedCornerShape(AppRadius.xxl)
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(AppIconSize.artwork * 5)
                .rotate(rotation)
                .background(
                    brush = Brush.linearGradient(
                        listOf(color, MaterialTheme.colorScheme.surfaceVariant)
                    ),
                    shape = RoundedCornerShape(AppRadius.xxl)
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(AppIconSize.xl * 2)
                    .background(AppColors.Scrim, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.MusicNote,
                    contentDescription = null,
                    tint = AppColors.ArtworkContent,
                    modifier = Modifier.size(AppIconSize.xl)
                )
            }
        }
    }
}
