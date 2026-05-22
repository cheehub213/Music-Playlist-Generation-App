package com.aurabeat.data.remote.dto

data class MoodPromptRequest(
    val prompt: String
)

data class MoodAnalysisResponse(
    val mood: String,
    val emotions: List<String>,
    val genres: List<String>,
    val energy: String,
    val tempo: String,
    val vibe: String,
    val keywords: List<String>,
    val playlistTitle: String,
    val source: String
)
