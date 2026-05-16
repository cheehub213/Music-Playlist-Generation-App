package com.aurabeat.data.repository

import com.aurabeat.domain.model.Album
import com.aurabeat.domain.model.Artist
import com.aurabeat.domain.model.Playlist
import com.aurabeat.domain.model.Song
import com.aurabeat.domain.model.User

/**
 * Central in-memory catalog used by all fake backend layers.
 * Replace this catalog with API responses later and keep the same repository contracts.
 */
object FakeDataProvider {
    private val songs = listOf(
        Song(
            id = "midnight-echoes",
            title = "Midnight Echoes",
            artist = "AuraBeat AI",
            album = "Midnight Echoes",
            durationSeconds = 222,
            artworkColor = 0xFF7C4DFF,
            artworkUrl = "https://images.aurabeat.local/tracks/midnight-echoes.jpg",
            genre = "Synthwave",
            isExplicit = false
        ),
        Song(
            id = "neon-dreams",
            title = "Neon Dreams",
            artist = "Neon Valley",
            album = "City Lights",
            durationSeconds = 198,
            artworkColor = 0xFFFF4081,
            artworkUrl = "https://images.aurabeat.local/tracks/neon-dreams.jpg",
            genre = "Electro Pop"
        ),
        Song(
            id = "soft-horizons",
            title = "Soft Horizons",
            artist = "Luna Parks",
            album = "Quiet Moods",
            durationSeconds = 245,
            artworkColor = 0xFF00B8D4,
            artworkUrl = "https://images.aurabeat.local/tracks/soft-horizons.jpg",
            genre = "Lo-fi"
        ),
        Song(
            id = "afterglow-city",
            title = "Afterglow City",
            artist = "Neon Valley",
            album = "Afterglow",
            durationSeconds = 224,
            artworkColor = 0xFF1E88E5,
            artworkUrl = "https://images.aurabeat.local/tracks/afterglow-city.jpg",
            genre = "Dance Pop"
        ),
        Song(
            id = "golden-static",
            title = "Golden Static",
            artist = "AuraBeat AI",
            album = "Signal Bloom",
            durationSeconds = 191,
            artworkColor = 0xFFFFB300,
            artworkUrl = "https://images.aurabeat.local/tracks/golden-static.jpg",
            genre = "Alt Pop"
        ),
        Song(
            id = "rain-window",
            title = "Rain Window",
            artist = "Studio Sora",
            album = "Rainy Study Session",
            durationSeconds = 233,
            artworkColor = 0xFF039BE5,
            artworkUrl = "https://images.aurabeat.local/tracks/rain-window.jpg",
            genre = "Ambient"
        )
    )

    private val artists = listOf(
        Artist(
            id = "aurabeat-ai",
            name = "AuraBeat AI",
            bio = "A fictional AI artist profile used to simulate generated recommendations.",
            artworkUrl = "https://images.aurabeat.local/artists/aurabeat-ai.jpg",
            genres = listOf("Synthwave", "Alt Pop"),
            followers = "2.4M",
            listeners = "8.1M"
        ),
        Artist(
            id = "neon-valley",
            name = "Neon Valley",
            bio = "A neon-soaked pop project with glossy hooks and late-night energy.",
            artworkUrl = "https://images.aurabeat.local/artists/neon-valley.jpg",
            genres = listOf("Electro Pop", "Dance Pop"),
            followers = "1.8M",
            listeners = "6.7M"
        ),
        Artist(
            id = "luna-parks",
            name = "Luna Parks",
            bio = "Lo-fi producer focusing on slow textures and study-friendly atmospheres.",
            artworkUrl = "https://images.aurabeat.local/artists/luna-parks.jpg",
            genres = listOf("Lo-fi", "Ambient"),
            followers = "842K",
            listeners = "3.2M"
        ),
        Artist(
            id = "studio-sora",
            name = "Studio Sora",
            bio = "Soft ambient arrangements for focused nights and rainy windows.",
            artworkUrl = "https://images.aurabeat.local/artists/studio-sora.jpg",
            genres = listOf("Ambient", "Study Beats"),
            followers = "521K",
            listeners = "2.1M"
        )
    )

    private val albums = listOf(
        Album("midnight-echoes", "Midnight Echoes", "AuraBeat AI", 2025, "https://images.aurabeat.local/albums/midnight-echoes.jpg", 10),
        Album("city-lights", "City Lights", "Neon Valley", 2024, "https://images.aurabeat.local/albums/city-lights.jpg", 12),
        Album("quiet-moods", "Quiet Moods", "Luna Parks", 2024, "https://images.aurabeat.local/albums/quiet-moods.jpg", 9),
        Album("rainy-study-session", "Rainy Study Session", "Studio Sora", 2023, "https://images.aurabeat.local/albums/rainy-study-session.jpg", 8)
    )

    private val playlists = listOf(
        Playlist(
            id = "pl-1",
            title = "Midnight Nostalgia",
            description = "Late-night tracks with warm pads, slow drums, and dreamy textures.",
            tracks = songs.take(5),
            artworkUrl = "https://images.aurabeat.local/playlists/midnight-nostalgia.jpg",
            genres = listOf("Synthwave", "Dream Pop"),
            followers = 182_000
        ),
        Playlist(
            id = "pl-2",
            title = "Neon Dreams",
            description = "Polished synth pop and glowing basslines for the city at night.",
            tracks = songs.drop(1),
            artworkUrl = "https://images.aurabeat.local/playlists/neon-dreams.jpg",
            genres = listOf("Electro Pop", "Dance Pop"),
            followers = 245_000
        ),
        Playlist(
            id = "pl-3",
            title = "Rainy Study Session",
            description = "Gentle focus music with soft rhythms and low-key ambience.",
            tracks = songs.takeLast(3) + songs.take(1),
            artworkUrl = "https://images.aurabeat.local/playlists/rainy-study-session.jpg",
            genres = listOf("Lo-fi", "Ambient"),
            followers = 317_000
        )
    )

    fun sampleSongs(): List<Song> = songs

    fun samplePlaylists(): List<Playlist> = playlists

    fun sampleArtists(): List<Artist> = artists

    fun sampleAlbums(): List<Album> = albums

    fun sampleArtist(id: String): Artist =
        artists.firstOrNull { it.id == id } ?: artists.first()

    fun sampleUser(): User = User(
        id = "u-1",
        name = "Aura User",
        email = "user@aurabeat.app",
        avatarUrl = "https://images.aurabeat.local/users/aura-user.jpg",
        isPremium = true,
        favoriteGenres = listOf("Lo-fi", "Synthwave", "Ambient")
    )

    fun sampleMoodKeywords(prompt: String): List<String> = when {
        prompt.contains("nostalg", ignoreCase = true) -> listOf("warm", "memory", "late night", "glow")
        prompt.contains("study", ignoreCase = true) -> listOf("focus", "rain", "soft", "steady")
        prompt.contains("gym", ignoreCase = true) -> listOf("energy", "pulse", "motion", "drive")
        else -> listOf("vibes", "mix", "energy", "texture")
    }
}
