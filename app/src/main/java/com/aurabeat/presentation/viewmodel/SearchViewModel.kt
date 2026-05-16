package com.aurabeat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurabeat.data.backend.FakeSearchEngine
import com.aurabeat.domain.model.Song
import com.aurabeat.domain.model.Artist
import com.aurabeat.domain.model.Album
import com.aurabeat.domain.model.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val songResults: List<Song> = emptyList(),
    val artistResults: List<Artist> = emptyList(),
    val albumResults: List<Album> = emptyList(),
    val playlistResults: List<Playlist> = emptyList(),
    val recentSearches: List<String> = listOf("Neon", "Study", "Chill"),
    val error: String? = null
)

class SearchViewModel(
    private val searchEngine: FakeSearchEngine = FakeSearchEngine()
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        search(query)
    }

    fun search(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    songResults = emptyList(),
                    artistResults = emptyList(),
                    albumResults = emptyList(),
                    playlistResults = emptyList(),
                    error = null
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = searchEngine.searchAll(query)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    songResults = results.songs,
                    artistResults = results.artists,
                    albumResults = results.albums,
                    playlistResults = results.playlists,
                    error = if (results.songs.isEmpty() && results.artists.isEmpty() && results.albums.isEmpty() && results.playlists.isEmpty()) {
                        "No matches found"
                    } else null
                )
            } catch (throwable: Throwable) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = throwable.message ?: "Search failed")
            }
        }
    }

    fun clearQuery() {
        _uiState.value = _uiState.value.copy(
            query = "",
            songResults = emptyList(),
            artistResults = emptyList(),
            albumResults = emptyList(),
            playlistResults = emptyList(),
            error = null
        )
    }
}
