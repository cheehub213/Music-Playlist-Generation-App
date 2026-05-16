package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.data.backend.FakeBackendSimulator
import com.aurabeat.domain.model.MoodAnalysis
import com.aurabeat.domain.model.Playlist

class FakeMoodRepository : MoodRepository {
    override suspend fun generatePlaylist(prompt: String): Resource<Playlist> {
        return FakeBackendSimulator.request("AI mood playlist", minDelayMs = 650L, maxDelayMs = 1400L, failureRate = 0.14f) {
            val keywords = FakeDataProvider.sampleMoodKeywords(prompt)
            val basePlaylist = when {
                prompt.contains("nostalg", ignoreCase = true) -> FakeDataProvider.samplePlaylists()[0]
                prompt.contains("study", ignoreCase = true) -> FakeDataProvider.samplePlaylists()[2]
                prompt.contains("gym", ignoreCase = true) -> FakeDataProvider.samplePlaylists()[1]
                else -> FakeDataProvider.samplePlaylists().random()
            }
            basePlaylist.copy(
                title = if (prompt.isBlank()) basePlaylist.title else "${basePlaylist.title} for ${prompt.trim().take(24)}",
                description = "Generated from your mood signal: ${keywords.joinToString()}.",
                genres = keywords.take(2),
                followers = basePlaylist.followers + (prompt.length * 37)
            )
        }
    }

    override suspend fun analyzeMood(prompt: String): Resource<MoodAnalysis> {
        return FakeBackendSimulator.request("Mood analysis", minDelayMs = 260L, maxDelayMs = 640L, failureRate = 0.08f) {
            val keywords = FakeDataProvider.sampleMoodKeywords(prompt)
            val mood = when {
                prompt.contains("nostalg", ignoreCase = true) -> "Nostalgic"
                prompt.contains("study", ignoreCase = true) -> "Focused"
                prompt.contains("gym", ignoreCase = true) -> "Energized"
                prompt.contains("calm", ignoreCase = true) -> "Calm"
                else -> "Mixed"
            }
            MoodAnalysis(
                mood = mood,
                energy = when (mood) {
                    "Energized" -> 0.9f
                    "Focused" -> 0.5f
                    "Nostalgic" -> 0.35f
                    else -> 0.6f
                },
                keywords = keywords,
                moodTags = keywords.take(3),
                genres = listOf("Synthwave", "Lo-fi", "Ambient").shuffled().take(2),
                recommendations = FakeDataProvider.sampleSongs().shuffled().take(3).map { it.title }
            )
        }
    }
}
