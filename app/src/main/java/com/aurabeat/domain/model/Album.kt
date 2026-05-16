package com.aurabeat.domain.model

data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val year: Int,
    val artworkUrl: String = "",
    val tracks: Int = 0
)
