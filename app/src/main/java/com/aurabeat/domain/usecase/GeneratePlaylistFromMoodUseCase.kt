package com.aurabeat.domain.usecase

import com.aurabeat.domain.model.Track

class GeneratePlaylistFromMoodUseCase {
    suspend operator fun invoke(mood: String): List<Track> {
        return emptyList()
    }
}
