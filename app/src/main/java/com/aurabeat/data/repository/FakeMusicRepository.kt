package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.data.backend.FakeBackendSimulator
import com.aurabeat.domain.model.Song

class FakeMusicRepository : MusicRepository {
    override suspend fun getTopSongs(): Resource<List<Song>> {
        return FakeBackendSimulator.request("Top songs") {
            FakeDataProvider.sampleSongs().shuffled().take(5)
        }
    }

    override suspend fun getSongById(id: String): Resource<Song> {
        return FakeBackendSimulator.request("Song details", minDelayMs = 160L, maxDelayMs = 360L) {
            FakeDataProvider.sampleSongs().firstOrNull { it.id == id }
                ?: FakeDataProvider.sampleSongs().random()
        }
    }
}
