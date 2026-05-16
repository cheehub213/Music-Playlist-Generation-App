package com.aurabeat.presentation.ui.model

import androidx.compose.ui.graphics.Color

enum class LibraryFilter(val label: String) {
    Playlists("Playlists"),
    Artists("Artists"),
    Albums("Albums"),
    Podcasts("Podcasts"),
    Downloaded("Downloaded")
}

data class RecentlyPlayedItem(
    val id: String,
    val title: String,
    val description: String,
    val color: Color,
    val filter: LibraryFilter
)

data class FavoriteSongItem(
    val id: String,
    val title: String,
    val artist: String,
    val duration: String,
    val color: Color,
    val filter: LibraryFilter
)

object LibraryMockData {
    val recentlyPlayed = listOf(
        RecentlyPlayedItem("recent-1", "Night Drive", "Synthwave after dark", Color(0xFF3949AB), LibraryFilter.Playlists),
        RecentlyPlayedItem("recent-2", "Chill Focus", "Soft beats for deep work", Color(0xFF00897B), LibraryFilter.Playlists),
        RecentlyPlayedItem("recent-3", "Gym Energy", "High tempo workout mix", Color(0xFFF4511E), LibraryFilter.Downloaded),
        RecentlyPlayedItem("recent-4", "Sad Hours", "Emotional late-night songs", Color(0xFF5E35B1), LibraryFilter.Albums),
        RecentlyPlayedItem("recent-5", "Late Night Jazz", "Warm piano and brass", Color(0xFF6D4C41), LibraryFilter.Artists)
    )

    val favoriteSongs = listOf(
        FavoriteSongItem("afterglow-city", "Afterglow City", "Neon Valley", "3:42", Color(0xFF1E88E5), LibraryFilter.Playlists),
        FavoriteSongItem("soft-horizons", "Soft Horizons", "Luna Parks", "4:05", Color(0xFF43A047), LibraryFilter.Downloaded),
        FavoriteSongItem("neon-dreams", "Neon Dreams", "Neon Valley", "3:18", Color(0xFFE53935), LibraryFilter.Artists),
        FavoriteSongItem("midnight-echoes", "Midnight Echoes", "AuraBeat AI", "3:42", Color(0xFF8E24AA), LibraryFilter.Albums),
        FavoriteSongItem("golden-static", "Golden Static", "AuraBeat AI", "3:11", Color(0xFFFFB300), LibraryFilter.Podcasts)
    )
}
