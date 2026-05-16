package com.aurabeat.data.repository

import com.aurabeat.domain.model.Playlist
import com.aurabeat.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * In-memory library state used to simulate save/favorite/recent/download behavior.
 * This can later be replaced by a local database or synced backend without changing UI state contracts.
 */
object FakeLibraryStore {
    data class LibraryState(
        val savedPlaylists: List<Playlist> = emptyList(),
        val favoriteSongIds: Set<String> = emptySet(),
        val recentlyPlayed: List<Song> = emptyList(),
        val downloadedSongs: List<Song> = emptyList()
    )

    private val libraryFlow = MutableStateFlow(LibraryState())
    val libraryState: StateFlow<LibraryState> = libraryFlow

    fun savePlaylist(playlist: Playlist) {
        val current = libraryFlow.value.savedPlaylists
        if (current.none { it.id == playlist.id }) {
            libraryFlow.value = libraryFlow.value.copy(savedPlaylists = current + playlist)
        }
    }

    fun toggleFavorite(song: Song) {
        val current = libraryFlow.value.favoriteSongIds
        libraryFlow.value = libraryFlow.value.copy(
            favoriteSongIds = if (current.contains(song.id)) current - song.id else current + song.id
        )
    }

    fun markRecentlyPlayed(song: Song) {
        val updated = (listOf(song) + libraryFlow.value.recentlyPlayed)
            .distinctBy { it.id }
            .take(12)
        libraryFlow.value = libraryFlow.value.copy(recentlyPlayed = updated)
    }

    fun markDownloaded(song: Song) {
        val current = libraryFlow.value.downloadedSongs
        if (current.none { it.id == song.id }) {
            libraryFlow.value = libraryFlow.value.copy(downloadedSongs = current + song)
        }
    }
}
