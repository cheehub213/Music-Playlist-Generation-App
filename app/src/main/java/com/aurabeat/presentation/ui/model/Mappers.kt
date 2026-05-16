package com.aurabeat.presentation.ui.model

import com.aurabeat.domain.model.Playlist
import com.aurabeat.domain.model.Song

fun MoodTrack.toDomain(): Song = Song(
    id = id,
    title = title,
    artist = artist,
    album = "",
    durationSeconds = durationSeconds,
    artworkColor = artworkColor
)

fun MoodPlaylist.toDomain(): Playlist = Playlist(
    id = id,
    title = title,
    description = description,
    tracks = tracks.map { it.toDomain() }
)
