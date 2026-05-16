package com.aurabeat.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aurabeat.presentation.ui.component.DownloadedMusicCard
import com.aurabeat.presentation.ui.component.FavoriteSongRow
import com.aurabeat.presentation.ui.component.LibraryFilterChips
import com.aurabeat.presentation.ui.component.RecentlyPlayedCard
import com.aurabeat.presentation.ui.component.SectionTitle
import com.aurabeat.presentation.ui.component.EmptyState
import com.aurabeat.presentation.ui.model.LibraryFilter
import com.aurabeat.presentation.ui.model.LibraryMockData
import com.aurabeat.presentation.ui.theme.AppLayout
import com.aurabeat.presentation.ui.theme.AppSpacing
import com.aurabeat.presentation.ui.theme.auraScreenGradient
import com.aurabeat.presentation.viewmodel.LibraryFilterType
import com.aurabeat.presentation.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(
    contentPadding: PaddingValues = WindowInsets.statusBars.asPaddingValues(),
    libraryViewModel: LibraryViewModel = remember { LibraryViewModel() },
    onSongClick: (String) -> Unit = {}
) {
    val state by libraryViewModel.uiState.collectAsState()
    var selectedFilter by rememberSaveable { mutableStateOf(LibraryFilter.Playlists) }

    val filteredRecentlyPlayed by remember(selectedFilter) {
        derivedStateOf {
            LibraryMockData.recentlyPlayed.filter { item ->
                selectedFilter == LibraryFilter.Playlists || item.filter == selectedFilter
            }
        }
    }
    val filteredSongs by remember(selectedFilter) {
        derivedStateOf {
            LibraryMockData.favoriteSongs.filter { song ->
                selectedFilter == LibraryFilter.Playlists || song.filter == selectedFilter
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(auraScreenGradient())
            .padding(contentPadding)
    ) {
        if (state.playlists.isEmpty() && state.songs.isEmpty()) {
            EmptyLibraryState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = AppLayout.screenHorizontalPadding,
                    vertical = AppLayout.screenVerticalPadding
                ),
                verticalArrangement = Arrangement.spacedBy(AppLayout.sectionSpacing)
            ) {
                item { LibraryHeader() }

                item {
                    LibraryFilterChips(
                        selectedFilter = selectedFilter,
                        onFilterSelected = {
                            selectedFilter = it
                            libraryViewModel.onFilterSelected(
                                if (it == LibraryFilter.Playlists) LibraryFilterType.Playlists else LibraryFilterType.Songs
                            )
                        }
                    )
                }

                item {
                    DownloadedMusicCard(downloadedCount = 24)
                }

                item {
                    SectionTitle(title = "Recently Played")
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                        items(filteredRecentlyPlayed, key = { it.id }) { item ->
                            RecentlyPlayedCard(
                                item = item,
                                modifier = Modifier.fillParentMaxWidth(0.46f)
                            )
                        }
                    }
                }

                item {
                    SectionTitle(title = "Favorite Songs")
                }

                item {
                    AnimatedVisibility(visible = filteredSongs.isEmpty()) {
                        Text(
                            text = "No favorites match this filter yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(filteredSongs, key = { it.id }) { song ->
                    FavoriteSongRow(
                        song = song,
                        onClick = { onSongClick(song.id) },
                        onPlayClick = { onSongClick(song.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Your Library",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your playlists, favorites, and recently played music",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun EmptyLibraryState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyState(
            icon = Icons.Rounded.LibraryMusic,
            title = "Your music collection is waiting for you",
            message = "Save songs and playlists to build a library that matches your mood."
        )
    }
}
