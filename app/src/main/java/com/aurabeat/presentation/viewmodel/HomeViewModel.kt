package com.aurabeat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.Color
import com.aurabeat.core.Resource
import com.aurabeat.data.repository.FakePlaylistRepository
import com.aurabeat.domain.model.Playlist
import com.aurabeat.presentation.ui.model.TrendingPlaylistItem
import com.aurabeat.presentation.ui.model.MoodDiscoveryMockData
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HomeUiState(
    val isLoading: Boolean = false,
    val trendingPlaylists: List<TrendingPlaylistItem> = MoodDiscoveryMockData.trendingPlaylists,
    val error: String? = null
)

class HomeViewModel(
    private val playlistRepository: FakePlaylistRepository = FakePlaylistRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadTrendingPlaylists()
    }

    fun loadTrendingPlaylists() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = playlistRepository.getFeaturedPlaylists()) {
                is Resource.Success<*> -> {
                    val playlists = result.data as List<Playlist>
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        trendingPlaylists = playlists.map { it.toTrendingItem() }
                    )
                }
                is Resource.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = result.message ?: "Unable to load playlists")
                Resource.Loading -> Unit
            }
        }
    }
}

private fun Playlist.toTrendingItem(): TrendingPlaylistItem {
    return TrendingPlaylistItem(
        id = id,
        title = title,
        description = description,
        coverColor = Color(tracks.firstOrNull()?.artworkColor ?: 0xFF7C4DFF)
    )
}
