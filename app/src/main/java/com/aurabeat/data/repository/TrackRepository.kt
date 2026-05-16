package com.aurabeat.data.repository

import com.aurabeat.data.remote.AuraBeatApiService
import com.aurabeat.domain.model.Track

class TrackRepository(
    private val api: AuraBeatApiService
) {
    suspend fun getTracks(): List<Track> {
        return api.getTracks().map { Track(it.id, it.title, it.artist) }
    }
}
