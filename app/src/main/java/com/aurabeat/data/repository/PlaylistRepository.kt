package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.domain.model.Playlist

interface PlaylistRepository {
    suspend fun getFeaturedPlaylists(): Resource<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist): Resource<Unit>
}
