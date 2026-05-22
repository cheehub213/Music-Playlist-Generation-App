package com.aurabeat.data.remote.dto

data class TrackResponse(
    val id: String,
    val title: String,
    val artist: String,
    val album: String? = null,
    val durationMs: Int? = null,
    val uri: String? = null,
    val previewUrl: String? = null,
    val externalUrl: String? = null,
    val artworkUrl: String? = null,
    val popularity: Int? = null,
    val source: String? = null
)
