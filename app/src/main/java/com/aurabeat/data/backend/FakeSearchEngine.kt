package com.aurabeat.data.backend

import com.aurabeat.data.repository.FakeDataProvider
import com.aurabeat.domain.model.Album
import com.aurabeat.domain.model.Artist
import com.aurabeat.domain.model.Playlist
import com.aurabeat.domain.model.Song

/**
 * Fake search engine that indexes the in-memory backend data.
 * Future real search can replace this class without touching presentation code.
 */
class FakeSearchEngine(
    private val dataProvider: FakeDataProvider = FakeDataProvider
) {
    suspend fun searchAll(query: String): FakeSearchResults = FakeSearchResults(
        songs = searchSongs(query),
        artists = searchArtists(query),
        albums = searchAlbums(query),
        playlists = searchPlaylists(query)
    )

    suspend fun searchSongs(query: String): List<Song> =
        FakeBackendSimulator.request("Song search", minDelayMs = 140L, maxDelayMs = 400L) {
            dataProvider.sampleSongs().filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.artist.contains(query, ignoreCase = true) ||
                    it.album.contains(query, ignoreCase = true) ||
                    it.genre.contains(query, ignoreCase = true)
            }
        }.getOrElseEmpty()

    suspend fun searchArtists(query: String): List<Artist> =
        FakeBackendSimulator.request("Artist search", minDelayMs = 140L, maxDelayMs = 450L) {
            dataProvider.sampleArtists().filter {
                it.name.contains(query, ignoreCase = true) ||
                    it.genres.any { genre -> genre.contains(query, ignoreCase = true) }
            }
        }.getOrElseEmpty()

    suspend fun searchAlbums(query: String): List<Album> =
        FakeBackendSimulator.request("Album search", minDelayMs = 140L, maxDelayMs = 450L) {
            dataProvider.sampleAlbums().filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.artist.contains(query, ignoreCase = true)
            }
        }.getOrElseEmpty()

    suspend fun searchPlaylists(query: String): List<Playlist> =
        FakeBackendSimulator.request("Playlist search", minDelayMs = 160L, maxDelayMs = 500L) {
            dataProvider.samplePlaylists().filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.genres.any { genre -> genre.contains(query, ignoreCase = true) }
            }
        }.getOrElseEmpty()
}

private fun <T> com.aurabeat.core.Resource<List<T>>.getOrElseEmpty(): List<T> {
    return when (this) {
        is com.aurabeat.core.Resource.Success -> data
        is com.aurabeat.core.Resource.Error -> emptyList()
        com.aurabeat.core.Resource.Loading -> emptyList()
    }
}
