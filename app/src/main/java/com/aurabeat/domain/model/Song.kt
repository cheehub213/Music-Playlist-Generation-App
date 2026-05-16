package com.aurabeat.domain.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Int,
    val artworkColor: Long,
    val artworkUrl: String = "",
    val genre: String = "",
    val isExplicit: Boolean = false
)
