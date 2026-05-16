package com.aurabeat.domain.usecase

import com.aurabeat.domain.model.Track

class SearchTracksUseCase {
    suspend operator fun invoke(query: String): List<Track> {
        return emptyList()
    }
}
