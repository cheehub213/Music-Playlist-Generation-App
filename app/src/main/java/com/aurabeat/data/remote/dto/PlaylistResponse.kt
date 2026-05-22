package com.aurabeat.data.remote.dto

data class PlaylistResponse(
    val id: String,
    val name: String,
    val title: String? = null,
    val description: String? = null,
    val coverImage: String? = null,
    val imageUrl: String? = null,
    val moodTags: List<String> = emptyList(),
    val energyScore: Double? = null,
    val tracks: List<TrackResponse> = emptyList(),
    val spotifySyncStatus: String? = null
)
