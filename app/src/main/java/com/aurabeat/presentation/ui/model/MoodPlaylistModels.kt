package com.aurabeat.presentation.ui.model

import kotlin.random.Random

data class MoodTrack(
    val id: String,
    val title: String,
    val artist: String,
    val durationSeconds: Int,
    val artworkColor: Long
)

data class MoodAnalysis(
    val mood: String,
    val energy: String,
    val vibe: String,
    val genres: List<String>,
    val keywords: List<String>
)

data class MoodPlaylist(
    val id: String,
    val title: String,
    val description: String,
    val moodTags: List<String>,
    val analysis: MoodAnalysis,
    val coverColor: Long,
    val tracks: List<MoodTrack>
)

object MoodPlaylistGenerator {
    private val playlists = listOf(
        MoodPlaylist(
            id = "midnight-nostalgia",
            title = "Midnight Nostalgia",
            description = "A dreamy collection of emotional late-night tracks.",
            moodTags = listOf("Nostalgic", "Late Night", "Dreamy"),
            analysis = MoodAnalysis(
                mood = "Nostalgic",
                energy = "Medium",
                vibe = "Cinematic",
                genres = listOf("Indie", "Synthwave", "Lo-fi"),
                keywords = listOf("memory", "glow", "slow burn", "retro")
            ),
            coverColor = 0xFF7C4DFF,
            tracks = listOf(
                MoodTrack("midnight-echoes", "Midnight Echoes", "AuraBeat AI", 222, 0xFF7C4DFF),
                MoodTrack("neon-dreams", "Neon Dreams", "Neon Valley", 198, 0xFFFF4081),
                MoodTrack("soft-horizons", "Soft Horizons", "Luna Parks", 245, 0xFF00B8D4),
                MoodTrack("afterglow-city", "Afterglow City", "Neon Valley", 224, 0xFF1E88E5),
                MoodTrack("golden-static", "Golden Static", "AuraBeat AI", 191, 0xFFFFB300)
            )
        ),
        MoodPlaylist(
            id = "neon-dreams",
            title = "Neon Dreams",
            description = "Electric synths and soft basslines for glowing nights.",
            moodTags = listOf("Neon", "Future", "City"),
            analysis = MoodAnalysis(
                mood = "Uplifted",
                energy = "Medium-High",
                vibe = "Futuristic",
                genres = listOf("Synthwave", "Electro", "Chillwave"),
                keywords = listOf("neon", "city lights", "motion", "vivid")
            ),
            coverColor = 0xFFFF4081,
            tracks = listOf(
                MoodTrack("city-spark", "City Spark", "Nova Lights", 210, 0xFFFF6D00),
                MoodTrack("neon-dreams", "Neon Dreams", "Neon Valley", 198, 0xFFFF4081),
                MoodTrack("pulse-grid", "Pulse Grid", "Circuit Bloom", 206, 0xFF00C853),
                MoodTrack("afterglow-city", "Afterglow City", "Neon Valley", 224, 0xFF1E88E5),
                MoodTrack("holo-sky", "Holo Sky", "Aurora Lane", 236, 0xFF00B8D4)
            )
        ),
        MoodPlaylist(
            id = "rainy-study",
            title = "Rainy Study Session",
            description = "Soft rhythms and rainy ambience to keep you focused.",
            moodTags = listOf("Focus", "Rainy", "Calm"),
            analysis = MoodAnalysis(
                mood = "Calm",
                energy = "Low",
                vibe = "Ambient",
                genres = listOf("Lo-fi", "Ambient", "Jazzhop"),
                keywords = listOf("rain", "focus", "soft", "coffee")
            ),
            coverColor = 0xFF00B8D4,
            tracks = listOf(
                MoodTrack("quiet-drift", "Quiet Drift", "Studio Sora", 208, 0xFF00B8D4),
                MoodTrack("rainy-window", "Rainy Window", "Loa", 230, 0xFF1E88E5),
                MoodTrack("soft-focus", "Soft Focus", "Paper Planes", 212, 0xFF7C4DFF),
                MoodTrack("study-lantern", "Study Lantern", "Sora", 201, 0xFF00C853),
                MoodTrack("grey-morning", "Grey Morning", "Northern Lights", 244, 0xFFFFB300)
            )
        ),
        MoodPlaylist(
            id = "after-hours-energy",
            title = "After Hours Energy",
            description = "Night-drive beats with a confident, electric edge.",
            moodTags = listOf("Energy", "Night Drive", "Bold"),
            analysis = MoodAnalysis(
                mood = "Energized",
                energy = "High",
                vibe = "Driving",
                genres = listOf("Electro", "House", "Synthwave"),
                keywords = listOf("drive", "rush", "neon", "pulse")
            ),
            coverColor = 0xFF00C853,
            tracks = listOf(
                MoodTrack("after-hours", "After Hours", "Pulse Club", 196, 0xFF00C853),
                MoodTrack("high-voltage", "High Voltage", "Neon Grid", 189, 0xFFFF6D00),
                MoodTrack("night-run", "Night Run", "Velocity", 205, 0xFF1E88E5),
                MoodTrack("signal-bloom", "Signal Bloom", "AuraBeat AI", 219, 0xFF7C4DFF),
                MoodTrack("city-heat", "City Heat", "Flux", 214, 0xFFFF4081)
            )
        ),
        MoodPlaylist(
            id = "soft-focus-waves",
            title = "Soft Focus Waves",
            description = "Warm textures and slow grooves for a gentle headspace.",
            moodTags = listOf("Soft", "Warm", "Flow"),
            analysis = MoodAnalysis(
                mood = "Relaxed",
                energy = "Low-Medium",
                vibe = "Oceanic",
                genres = listOf("Chill", "Lo-fi", "Dream Pop"),
                keywords = listOf("soft", "waves", "slow", "sunset")
            ),
            coverColor = 0xFFFFB300,
            tracks = listOf(
                MoodTrack("soft-focus", "Soft Focus", "Paper Planes", 212, 0xFF7C4DFF),
                MoodTrack("sunset-lines", "Sunset Lines", "Wavechild", 226, 0xFFFFB300),
                MoodTrack("sea-glass", "Sea Glass", "Lowtide", 238, 0xFF00B8D4),
                MoodTrack("warm-echo", "Warm Echo", "AuraBeat AI", 203, 0xFFFF4081),
                MoodTrack("coastline", "Coastline", "Lowtide", 220, 0xFF1E88E5)
            )
        )
    )

    fun generate(prompt: String, lastPlaylistId: String? = null): MoodPlaylist {
        val normalized = prompt.lowercase()
        val matched = when {
            normalized.contains("nostalg") || normalized.contains("late night") -> "midnight-nostalgia"
            normalized.contains("rain") || normalized.contains("study") || normalized.contains("focus") -> "rainy-study"
            normalized.contains("gym") || normalized.contains("energy") || normalized.contains("workout") -> "after-hours-energy"
            normalized.contains("neon") || normalized.contains("city") -> "neon-dreams"
            normalized.contains("soft") || normalized.contains("relax") || normalized.contains("calm") -> "soft-focus-waves"
            else -> null
        }

        val candidates = playlists.filterNot { it.id == lastPlaylistId }
        val picked = candidates.firstOrNull { it.id == matched } ?: candidates.random(Random(prompt.hashCode()))
        return picked
    }

    fun allPlaylists(): List<MoodPlaylist> = playlists
}
