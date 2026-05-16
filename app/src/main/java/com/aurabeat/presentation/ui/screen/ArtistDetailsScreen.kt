package com.aurabeat.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.aurabeat.presentation.ui.component.AlbumCard
import com.aurabeat.presentation.ui.component.AppCard
import com.aurabeat.presentation.ui.component.ArtistHeader
import com.aurabeat.presentation.ui.component.ArtistStatsCard
import com.aurabeat.presentation.ui.component.PopularSongRow
import com.aurabeat.presentation.ui.component.RelatedArtistCard
import com.aurabeat.presentation.ui.component.SectionTitle
import com.aurabeat.presentation.ui.theme.AppLayout
import com.aurabeat.presentation.ui.theme.AppSpacing
import com.aurabeat.presentation.ui.theme.auraScreenGradient
import com.aurabeat.presentation.viewmodel.ArtistViewModel

@Composable
fun ArtistDetailsScreen(
    artistId: String,
    artistViewModel: ArtistViewModel,
    contentPadding: PaddingValues = WindowInsets.statusBars.asPaddingValues(),
    onBack: () -> Unit,
    onSongClick: (String) -> Unit,
    onAlbumClick: (String) -> Unit,
    onArtistClick: (String) -> Unit
) {
    LaunchedEffect(artistId) {
        artistViewModel.loadArtist(artistId)
    }

    val state by artistViewModel.uiState.collectAsState()
    val artist = state.artist
    val listState = rememberLazyListState()
    val collapsedFraction by remember {
        derivedStateOf {
            ((listState.firstVisibleItemScrollOffset / 340f) + listState.firstVisibleItemIndex).coerceIn(0f, 1f)
        }
    }
    val titleAlpha by animateFloatAsState(
        targetValue = if (collapsedFraction > 0.62f) 1f else 0f,
        label = "artist_toolbar_title_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(auraScreenGradient())
            .padding(contentPadding)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = AppLayout.screenHorizontalPadding,
                vertical = AppLayout.screenVerticalPadding
            ),
            verticalArrangement = Arrangement.spacedBy(AppLayout.sectionSpacing)
        ) {
            item {
                ArtistHeader(
                    artist = artist,
                    collapsedFraction = collapsedFraction,
                    isFollowing = state.isFollowing,
                    onFollowClick = artistViewModel::toggleFollow,
                    onPlayClick = { artist.songs.firstOrNull()?.let { onSongClick(it.id) } },
                    onShuffleClick = { artist.songs.randomOrNull()?.let { onSongClick(it.id) } }
                )
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                    item { ArtistStatsCard(value = artist.monthlyListeners, label = "Listeners") }
                    item { ArtistStatsCard(value = artist.followers, label = "Followers") }
                    item { ArtistStatsCard(value = artist.totalAlbums, label = "Albums") }
                    item { ArtistStatsCard(value = artist.totalSongs, label = "Songs") }
                }
            }

            item {
                ArtistBiographySection(
                    biography = artist.biography,
                    country = artist.country,
                    debutYear = artist.debutYear,
                    genres = artist.genres,
                    expanded = state.isBioExpanded,
                    onToggleExpanded = artistViewModel::toggleBioExpanded
                )
            }

            item { SectionTitle(title = "Popular Songs") }

            items(artist.songs, key = { it.id }) { song ->
                PopularSongRow(
                    index = artist.songs.indexOf(song) + 1,
                    song = song,
                    onClick = { onSongClick(song.id) }
                )
            }

            item { SectionTitle(title = "Albums") }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                    items(artist.albums, key = { it.id }) { album ->
                        AlbumCard(
                            album = album,
                            onClick = { onAlbumClick(album.id) },
                            modifier = Modifier.fillParentMaxWidth(0.42f)
                        )
                    }
                }
            }

            item { SectionTitle(title = "Related Artists") }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                    items(artist.relatedArtists, key = { it.id }) { related ->
                        RelatedArtistCard(
                            artist = related,
                            onClick = { onArtistClick(related.id) },
                            modifier = Modifier.fillParentMaxWidth(0.42f)
                        )
                    }
                }
            }
        }

        ArtistStickyToolbar(
            title = artist.name,
            titleAlpha = titleAlpha,
            onBack = onBack
        )
    }
}

@Composable
private fun ArtistStickyToolbar(
    title: String,
    titleAlpha: Float,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = titleAlpha * 0.92f))
            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
        }
        AnimatedVisibility(visible = titleAlpha > 0.5f) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ArtistBiographySection(
    biography: String,
    country: String,
    debutYear: String,
    genres: List<String>,
    expanded: Boolean,
    onToggleExpanded: () -> Unit
) {
    AppCard {
        Column(
            modifier = Modifier.padding(AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            SectionTitle(title = "About")
            Text(
                text = if (expanded) biography else biography.take(132) + "...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                Text(
                    text = country,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Debut $debutYear",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = genres.joinToString(" - "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onToggleExpanded) {
                Text(if (expanded) "Show Less" else "Read More")
            }
        }
    }
}
