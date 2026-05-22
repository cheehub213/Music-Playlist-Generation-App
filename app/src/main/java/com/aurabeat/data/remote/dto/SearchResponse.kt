package com.aurabeat.data.remote.dto

data class SearchResponse(
    val tracks: List<TrackResponse> = emptyList(),
    val artists: List<ArtistResponse> = emptyList(),
    val albums: List<AlbumResponse> = emptyList(),
    val playlists: List<PlaylistResponse> = emptyList()
)

data class ArtistResponse(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val popularity: Int? = null
)

data class AlbumResponse(
    val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String? = null,
    val releaseDate: String? = null
)

data class FeaturedPlaylistsResponse(
    val playlists: List<PlaylistResponse> = emptyList()
)
