package com.aurabeat.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurabeat.presentation.ui.component.AuraBeatSearchBar
import com.aurabeat.presentation.ui.component.CategoryCard
import com.aurabeat.presentation.ui.component.ErrorState
import com.aurabeat.presentation.ui.component.SearchResultCard
import com.aurabeat.presentation.ui.component.SectionTitle
import com.aurabeat.presentation.ui.model.SearchMockData
import com.aurabeat.presentation.ui.model.SearchResultItem
import com.aurabeat.presentation.ui.theme.AppLayout
import com.aurabeat.presentation.ui.theme.AppSpacing
import com.aurabeat.presentation.ui.theme.auraScreenGradient
import com.aurabeat.presentation.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    contentPadding: PaddingValues = WindowInsets.statusBars.asPaddingValues(),
    searchViewModel: SearchViewModel = androidx.compose.runtime.remember { SearchViewModel() },
    onSongClick: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {}
) {
    val state by searchViewModel.uiState.collectAsState()
    val songResults = remember(state.songResults) {
        state.songResults.map { song ->
            SearchResultItem(
                id = song.id,
                title = song.title,
                artist = song.artist,
                imageColor = Color(song.artworkColor)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(auraScreenGradient())
            .padding(contentPadding)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = AppLayout.screenHorizontalPadding,
                vertical = AppLayout.screenVerticalPadding
            ),
            verticalArrangement = Arrangement.spacedBy(AppLayout.sectionSpacing)
        ) {
            item { SearchHeader() }

            item {
                AuraBeatSearchBar(
                    query = state.query,
                    onQueryChange = searchViewModel::onQueryChanged
                )
            }

            if (state.query.isNotBlank()) {
                item {
                    Text(
                        text = "Matches: ${state.songResults.size} songs • ${state.artistResults.size} artists • ${state.albumResults.size} albums • ${state.playlistResults.size} playlists",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (state.isLoading) {
                item {
                    Text(
                        text = "Searching the fake catalog...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            state.error?.let { error ->
                item {
                    ErrorState(
                        message = error,
                        onRetry = { searchViewModel.search(state.query) }
                    )
                }
            }

            item {
                AnimatedVisibility(visible = state.query.isBlank()) {
                    RecentSearchesSection(
                        recentSearches = state.recentSearches,
                        onRecentClick = searchViewModel::onQueryChanged
                    )
                }
            }

            item {
                SectionTitle(title = "Browse Categories")
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
                    userScrollEnabled = false
                ) {
                    items(SearchMockData.categories) { category ->
                        CategoryCard(category = category)
                    }
                }
            }

            item {
                AnimatedVisibility(visible = state.query.isNotBlank()) {
                    Text(
                        text = "Search Results",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (state.query.isNotBlank() && songResults.isEmpty() && state.error == null && !state.isLoading) {
                item {
                    Text(
                        text = "No mock results for \"${state.query}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(songResults, key = { it.id }) { result ->
                SearchResultCard(
                    result = result,
                    onClick = { onSongClick(result.id) },
                    onPlayClick = { onSongClick(result.id) },
                    onArtistClick = { onArtistClick(result.artist.toArtistId()) }
                )
            }
        }
    }
}

private fun String.toArtistId(): String = lowercase()
    .replace("&", "and")
    .replace(Regex("[^a-z0-9]+"), "-")
    .trim('-')

@Composable
private fun SearchHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
        Text(
            text = "Search",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Find songs, artists, albums, and playlists",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RecentSearchesSection(
    recentSearches: List<String>,
    onRecentClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
        SectionTitle(title = "Recent Searches")

        recentSearches.forEach { recent ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRecentClick(recent) }
                    .padding(vertical = AppSpacing.sm),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {
                Icon(
                    imageVector = Icons.Rounded.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = recent,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
