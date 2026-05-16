package com.aurabeat.domain.model

data class Playlist(
    val id: String,
    val title: String,
    val description: String,
    val tracks: List<Song>,
    val artworkUrl: String = "",
    val genres: List<String> = emptyList(),
    val followers: Int = 0
)
