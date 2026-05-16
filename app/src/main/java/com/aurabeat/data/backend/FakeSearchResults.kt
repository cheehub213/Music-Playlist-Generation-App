package com.aurabeat.data.backend

import com.aurabeat.domain.model.Album
import com.aurabeat.domain.model.Artist
import com.aurabeat.domain.model.Playlist
import com.aurabeat.domain.model.Song

data class FakeSearchResults(
    val songs: List<Song> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val albums: List<Album> = emptyList(),
    val playlists: List<Playlist> = emptyList()
)
