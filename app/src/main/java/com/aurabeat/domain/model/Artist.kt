package com.aurabeat.domain.model

data class Artist(
    val id: String,
    val name: String,
    val bio: String? = null,
    val artworkUrl: String = "",
    val genres: List<String> = emptyList(),
    val followers: String = "",
    val listeners: String = ""
)
