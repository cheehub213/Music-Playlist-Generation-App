package com.aurabeat.presentation.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class UserProfile(
    val name: String,
    val username: String,
    val bio: String
)

data class ProfileStatItem(
    val label: String,
    val value: String
)

data class ProfileArtistItem(
    val id: String,
    val name: String,
    val subtitle: String,
    val color: Color
)

data class SettingsMenuItem(
    val title: String,
    val icon: ImageVector
)

object ProfileMockData {
    val user = UserProfile(
        name = "Mazen Bahri",
        username = "@aurabeat_user",
        bio = "Music lover - Night vibes - AI playlists"
    )

    val stats = listOf(
        ProfileStatItem("Playlists", "24"),
        ProfileStatItem("Followers", "1.2K"),
        ProfileStatItem("Following", "310"),
        ProfileStatItem("Liked Songs", "540")
    )

    val favoriteGenres = listOf(
        "Lo-fi",
        "Jazz",
        "Hip Hop",
        "Chill",
        "Electronic",
        "Ambient",
        "Indie"
    )

    val recentlyPlayedArtists = listOf(
        ProfileArtistItem("artist-1", "The Weeknd", "Dreamy pop rotation", Color(0xFF7E57C2)),
        ProfileArtistItem("artist-2", "Drake", "Late-night hits", Color(0xFF039BE5)),
        ProfileArtistItem("artist-3", "Arctic Monkeys", "Indie rock favorites", Color(0xFF546E7A)),
        ProfileArtistItem("artist-4", "Travis Scott", "High-energy tracks", Color(0xFFF4511E)),
        ProfileArtistItem("artist-5", "Lana Del Rey", "Cinematic mood", Color(0xFFD81B60))
    )

    val settings = listOf(
        SettingsMenuItem("Account Settings", Icons.Rounded.AccountCircle),
        SettingsMenuItem("Theme", Icons.Rounded.DarkMode),
        SettingsMenuItem("Notifications", Icons.Rounded.Notifications),
        SettingsMenuItem("Audio Quality", Icons.Rounded.GraphicEq),
        SettingsMenuItem("Downloads", Icons.Rounded.Download),
        SettingsMenuItem("Privacy", Icons.Rounded.Lock),
        SettingsMenuItem("Help & Support", Icons.AutoMirrored.Rounded.Help),
        SettingsMenuItem("Logout", Icons.AutoMirrored.Rounded.Logout)
    )
}
