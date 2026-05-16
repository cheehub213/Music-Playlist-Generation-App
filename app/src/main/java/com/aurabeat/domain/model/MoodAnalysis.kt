package com.aurabeat.domain.model

data class MoodAnalysis(
    val mood: String,
    val energy: Float,
    val keywords: List<String>,
    val moodTags: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val recommendations: List<String> = emptyList()
)
