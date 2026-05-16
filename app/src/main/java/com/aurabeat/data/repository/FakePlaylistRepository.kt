package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.data.backend.FakeBackendSimulator
import com.aurabeat.domain.model.Playlist

class FakePlaylistRepository : PlaylistRepository {
    override suspend fun getFeaturedPlaylists(): Resource<List<Playlist>> {
        return FakeBackendSimulator.request("Featured playlists") {
            FakeDataProvider.samplePlaylists().shuffled()
        }
    }

    override suspend fun savePlaylist(playlist: Playlist): Resource<Unit> {
        return FakeBackendSimulator.request("Save playlist", minDelayMs = 180L, maxDelayMs = 420L) {
            FakeLibraryStore.savePlaylist(playlist)
        }
    }
}
