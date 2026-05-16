package com.aurabeat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurabeat.core.Resource
import com.aurabeat.data.repository.FakePlaylistRepository
import com.aurabeat.data.repository.FakeMusicRepository
import com.aurabeat.domain.model.Playlist
import com.aurabeat.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class LibraryFilterType { Playlists, Songs }

data class LibraryUiState(
    val isLoading: Boolean = false,
    val selectedFilter: LibraryFilterType = LibraryFilterType.Playlists,
    val playlists: List<Playlist> = emptyList(),
    val songs: List<Song> = emptyList(),
    val error: String? = null
)

class LibraryViewModel(
    private val playlistRepository: FakePlaylistRepository = FakePlaylistRepository(),
    private val musicRepository: FakeMusicRepository = FakeMusicRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState

    init {
        loadLibrary()
    }

    fun loadLibrary() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val playlists = playlistRepository.getFeaturedPlaylists()
            val songs = musicRepository.getTopSongs()
            _uiState.value = when {
                playlists is Resource.Success<*> && songs is Resource.Success<*> -> _uiState.value.copy(
                    isLoading = false,
                    playlists = playlists.data as List<Playlist>,
                    songs = songs.data as List<Song>
                )
                playlists is Resource.Error -> _uiState.value.copy(isLoading = false, error = playlists.message)
                songs is Resource.Error -> _uiState.value.copy(isLoading = false, error = songs.message)
                else -> _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onFilterSelected(filter: LibraryFilterType) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }
}
