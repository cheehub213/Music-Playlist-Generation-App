package com.aurabeat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ArtistSong(
    val id: String,
    val title: String,
    val duration: String,
    val color: Long
)

data class ArtistAlbum(
    val id: String,
    val title: String,
    val releaseYear: String,
    val color: Long
)

data class RelatedArtist(
    val id: String,
    val name: String,
    val genre: String,
    val color: Long
)

data class ArtistDetails(
    val id: String,
    val name: String,
    val monthlyListeners: String,
    val followers: String,
    val totalAlbums: String,
    val totalSongs: String,
    val biography: String,
    val country: String,
    val debutYear: String,
    val genres: List<String>,
    val bannerColor: Long,
    val songs: List<ArtistSong>,
    val albums: List<ArtistAlbum>,
    val relatedArtists: List<RelatedArtist>
)

data class ArtistUiState(
    val artist: ArtistDetails = MockArtistData.artist,
    val isFollowing: Boolean = false,
    val isBioExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

object MockArtistData {
    val artist = ArtistDetails(
        id = "the-weeknd",
        name = "The Weeknd",
        monthlyListeners = "116.4M",
        followers = "78.2M",
        totalAlbums = "8",
        totalSongs = "143",
        biography = "The Weeknd is a genre-shaping artist known for cinematic pop, atmospheric R&B, and late-night synth textures. His music blends emotional storytelling, massive hooks, and immersive production that shaped a new era of modern streaming pop.",
        country = "Canada",
        debutYear = "2011",
        genres = listOf("Pop", "R&B", "Synthwave", "Alternative"),
        bannerColor = 0xFF6A1B9A,
        songs = listOf(
            ArtistSong("midnight-echoes", "Midnight Echoes", "3:42", 0xFF7C4DFF),
            ArtistSong("neon-dreams", "Neon Dreams", "3:18", 0xFFFF4081),
            ArtistSong("afterglow-city", "Afterglow City", "3:44", 0xFF1E88E5),
            ArtistSong("soft-horizons", "Soft Horizons", "4:05", 0xFF00B8D4),
            ArtistSong("golden-static", "Golden Static", "3:11", 0xFFFFB300)
        ),
        albums = listOf(
            ArtistAlbum("after-hours", "After Hours", "2020", 0xFFE53935),
            ArtistAlbum("dawn-fm", "Dawn FM", "2022", 0xFF00ACC1),
            ArtistAlbum("starboy", "Starboy", "2016", 0xFF5E35B1),
            ArtistAlbum("beauty", "Beauty Behind", "2015", 0xFF3949AB)
        ),
        relatedArtists = listOf(
            RelatedArtist("drake", "Drake", "Hip Hop", 0xFF039BE5),
            RelatedArtist("travis-scott", "Travis Scott", "Rap", 0xFFF4511E),
            RelatedArtist("lana-del-rey", "Lana Del Rey", "Alternative", 0xFFD81B60),
            RelatedArtist("arctic-monkeys", "Arctic Monkeys", "Indie Rock", 0xFF546E7A)
        )
    )
}

class ArtistViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState: StateFlow<ArtistUiState> = _uiState

    fun loadArtist(artistId: String) {
        _uiState.value = _uiState.value.copy(
            artist = MockArtistData.artist.copy(id = artistId.ifBlank { MockArtistData.artist.id })
        )
    }

    fun toggleFollow() {
        _uiState.value = _uiState.value.copy(isFollowing = !_uiState.value.isFollowing)
    }

    fun toggleBioExpanded() {
        _uiState.value = _uiState.value.copy(isBioExpanded = !_uiState.value.isBioExpanded)
    }
}
