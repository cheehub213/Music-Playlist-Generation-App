package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.data.backend.FakeBackendSimulator
import com.aurabeat.domain.model.Artist

class FakeArtistRepository : ArtistRepository {
    override suspend fun getArtist(id: String): Resource<Artist> {
        return FakeBackendSimulator.request("Artist details", minDelayMs = 160L, maxDelayMs = 480L) {
            FakeDataProvider.sampleArtist(id)
        }
    }
}
