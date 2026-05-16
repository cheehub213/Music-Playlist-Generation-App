package com.aurabeat.presentation.ui.model

import androidx.compose.ui.graphics.Color

data class SearchCategoryItem(
    val title: String,
    val colors: List<Color>
)

data class SearchResultItem(
    val id: String,
    val title: String,
    val artist: String,
    val imageColor: Color
)

object SearchMockData {
    val recentSearches = listOf(
        "The Weeknd",
        "Chill Playlist",
        "Drake",
        "Study Vibes"
    )

    val categories = listOf(
        SearchCategoryItem("Pop", listOf(Color(0xFFFF4B7D), Color(0xFF8E24AA))),
        SearchCategoryItem("Hip Hop", listOf(Color(0xFFFF8A00), Color(0xFFB00020))),
        SearchCategoryItem("Jazz", listOf(Color(0xFF00BFA5), Color(0xFF00574B))),
        SearchCategoryItem("Workout", listOf(Color(0xFF1DB954), Color(0xFF0B5D32))),
        SearchCategoryItem("Relax", listOf(Color(0xFF6A8DFF), Color(0xFF303F9F))),
        SearchCategoryItem("Sleep", listOf(Color(0xFF4E5D6C), Color(0xFF151A20))),
        SearchCategoryItem("Focus", listOf(Color(0xFF00C2FF), Color(0xFF005D80))),
        SearchCategoryItem("Electronic", listOf(Color(0xFFFFD600), Color(0xFFFF6D00)))
    )

    val results = listOf(
        SearchResultItem("midnight-echoes", "Midnight Echoes", "AuraBeat AI", Color(0xFFE53935)),
        SearchResultItem("neon-dreams", "Neon Dreams", "Neon Valley", Color(0xFF5E35B1)),
        SearchResultItem("soft-horizons", "Soft Horizons", "Luna Parks", Color(0xFF039BE5)),
        SearchResultItem("afterglow-city", "Afterglow City", "Neon Valley", Color(0xFF43A047)),
        SearchResultItem("golden-static", "Golden Static", "AuraBeat AI", Color(0xFF00897B))
    )
}
