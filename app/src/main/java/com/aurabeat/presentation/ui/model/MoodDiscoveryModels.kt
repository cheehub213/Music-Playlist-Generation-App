package com.aurabeat.presentation.ui.model

import androidx.compose.ui.graphics.Color

data class MoodChipItem(
    val label: String,
    val presetPrompt: String
)

data class TrendingPlaylistItem(
    val id: String,
    val title: String,
    val description: String,
    val coverColor: Color
)

object MoodDiscoveryMockData {
    val quickMoods = listOf(
        MoodChipItem("Chill", "Give me chill lo-fi and sunset vibes"),
        MoodChipItem("Focus", "I need deep focus music for coding"),
        MoodChipItem("Sad", "I feel nostalgic tonight"),
        MoodChipItem("Happy", "Play bright happy songs for a good mood"),
        MoodChipItem("Gym", "Give me energetic gym music"),
        MoodChipItem("Romantic", "Romantic dinner music with soft vocals"),
        MoodChipItem("Party", "High-energy party anthems"),
        MoodChipItem("Sleep", "Relaxing study vibes")
    )

    val trendingPlaylists = listOf(
        TrendingPlaylistItem("1", "Midnight Echoes", "Dreamy synthwave for late-night drives", Color(0xFF7C4DFF)),
        TrendingPlaylistItem("2", "Pulse Mode", "High BPM drops to keep your workout alive", Color(0xFF00C853)),
        TrendingPlaylistItem("3", "Quiet Focus", "Ambient instrumentals for deep concentration", Color(0xFF00B8D4)),
        TrendingPlaylistItem("4", "Soft Hearts", "Romantic indie pop and smooth soul", Color(0xFFFF4081)),
        TrendingPlaylistItem("5", "Neon Party", "Crowd favorites and dance-floor energy", Color(0xFFFF6D00))
    )
}

