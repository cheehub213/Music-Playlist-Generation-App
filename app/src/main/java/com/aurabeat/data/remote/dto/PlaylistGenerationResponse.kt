package com.aurabeat.data.remote.dto

data class PlaylistRequest(
    val title: String,
    val description: String? = null,
    val tracks: List<TrackResponse>
)

data class PlaylistGenerationResponse(
    val mood: String,
    val emotions: List<String>,
    val genres: List<String>,
    val energy: String,
    val vibe: String,
    val keywords: List<String>,
    val playlistTitle: String,
    val playlist: PlaylistDto
)

data class PlaylistDto(
    val id: String,
    val title: String,
    val description: String,
    val coverImage: String? = null,
    val moodTags: List<String>,
    val energyScore: Double,
    val tracks: List<TrackResponse>,
    val spotifySyncStatus: String
)
