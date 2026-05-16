package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.domain.model.MoodAnalysis
import com.aurabeat.domain.model.Playlist

interface MoodRepository {
    suspend fun generatePlaylist(prompt: String): Resource<Playlist>
    suspend fun analyzeMood(prompt: String): Resource<MoodAnalysis>
}
