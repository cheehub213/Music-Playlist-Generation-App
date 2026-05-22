package com.aurabeat.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.aurabeat.presentation.ui.component.AppChip
import com.aurabeat.presentation.ui.component.ArtistCard
import com.aurabeat.presentation.ui.component.PremiumBanner
import com.aurabeat.presentation.ui.component.ProfileHeader
import com.aurabeat.presentation.ui.component.SectionTitle
import com.aurabeat.presentation.ui.component.SettingsItem
import com.aurabeat.presentation.ui.component.StatsCard
import com.aurabeat.presentation.ui.model.ProfileMockData
import com.aurabeat.presentation.ui.theme.AppLayout
import com.aurabeat.presentation.ui.theme.AppSpacing
import com.aurabeat.presentation.ui.theme.auraScreenGradient
import com.aurabeat.presentation.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    contentPadding: PaddingValues = WindowInsets.statusBars.asPaddingValues(),
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = remember { ProfileViewModel() }
) {
    val state by profileViewModel.uiState.collectAsState()

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
            item {
                ProfileHeader(profile = ProfileMockData.user)
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                    items(ProfileMockData.stats) { stat ->
                        StatsCard(stat = stat)
                    }
                }
            }

            item {
                PremiumBanner()
            }

            item {
                SectionTitle(title = "Favorite Genres")
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                    items(ProfileMockData.favoriteGenres) { genre ->
                        val selected = state.selectedGenres.contains(genre)
                        AppChip(
                            text = genre,
                            selected = selected,
                            onClick = {
                                profileViewModel.toggleGenre(genre)
                            }
                        )
                    }
                }
            }

            item {
                SectionTitle(title = "Recently Played Artists")
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                    items(ProfileMockData.recentlyPlayedArtists, key = { it.id }) { artist ->
                        ArtistCard(
                            artist = artist,
                            modifier = Modifier.fillParentMaxWidth(0.42f)
                        )
                    }
                }
            }

            item {
                SectionTitle(title = "Settings")
            }

            items(ProfileMockData.settings) { item ->
                SettingsItem(
                    item = item,
                    isThemeToggle = item.title == "Theme",
                    isDarkTheme = isDarkTheme,
                    onClick = {
                        if (item.title == "Theme") {
                            onToggleTheme()
                        } else if (item.title == "Logout") {
                            onLogout()
                        } else {
                            // Future navigation/action hook: ProfileViewModel.onSettingsItemClicked(item.title)
                        }
                    }
                )
            }
        }
    }
}
