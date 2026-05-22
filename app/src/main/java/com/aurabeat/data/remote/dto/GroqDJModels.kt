package com.aurabeat.data.remote.dto

// Groq Request
data class GroqDJRequest(
    val prompt: String
)

// Groq Song Item
data class GroqSongItem(
    val title: String,
    val artist: String
)

// Groq DJ Playlist Response
data class GroqDJPlaylistResponse(
    val userId: String? = null,
    val inputPrompt: String,
    val generatedAt: String,
    val playlist: GroqDJPlaylist
)

data class GroqDJPlaylist(
    val id: String,
    val title: String,
    val description: String,
    val tracks: List<TrackResponse>,
    val spotifySyncStatus: String,
    val source: String
)

// Result sealed class for error handling
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class Loading<T>(val data: T? = null) : Result<T>()
}
