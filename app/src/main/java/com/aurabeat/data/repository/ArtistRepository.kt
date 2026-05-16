package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.domain.model.Artist

interface ArtistRepository {
    suspend fun getArtist(id: String): Resource<Artist>
}
