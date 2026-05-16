package com.aurabeat.domain.repository

import com.aurabeat.domain.model.Playlist

interface IPlaylistRepository {
    suspend fun getPlaylists(): List<Playlist>
}
