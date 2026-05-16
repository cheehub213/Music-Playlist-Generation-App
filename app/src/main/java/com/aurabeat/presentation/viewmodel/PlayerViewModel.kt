package com.aurabeat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MockSong(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Int,
    val artworkColor: Long
)

data class PlayerUiState(
    val songs: List<MockSong> = MockPlayerData.songs,
    val currentSong: MockSong? = MockPlayerData.songs.firstOrNull(),
    val isPlaying: Boolean = false,
    val isFavorite: Boolean = false,
    val isShuffleEnabled: Boolean = false,
    val isRepeatEnabled: Boolean = false,
    val progressSeconds: Int = 0
) {
    val queue: List<MockSong>
        get() {
            val currentIndex = songs.indexOfFirst { it.id == currentSong?.id }.coerceAtLeast(0)
            return songs.drop(currentIndex) + songs.take(currentIndex)
        }
}

object MockPlayerData {
    val songs = listOf(
        MockSong("midnight-echoes", "Midnight Echoes", "AuraBeat AI", "Midnight Echoes", 222, 0xFF7C4DFF),
        MockSong("neon-dreams", "Neon Dreams", "Neon Valley", "City Lights", 198, 0xFFFF4081),
        MockSong("soft-horizons", "Soft Horizons", "Luna Parks", "Quiet Moods", 245, 0xFF00B8D4),
        MockSong("afterglow-city", "Afterglow City", "Neon Valley", "Afterglow", 224, 0xFF1E88E5),
        MockSong("golden-static", "Golden Static", "AuraBeat AI", "Signal Bloom", 191, 0xFFFFB300)
    )
}

class PlayerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState

    fun playSong(songId: String) {
        viewModelScope.launch {
            val state = _uiState.value
            val song = state.songs.firstOrNull { it.id == songId } ?: state.currentSong ?: return@launch
            _uiState.value = state.copy(
                currentSong = song,
                isPlaying = true,
                progressSeconds = 0
            )
        }
    }

    fun playCustomQueue(tracks: List<MockSong>, songId: String) {
        if (tracks.isEmpty()) return
        viewModelScope.launch {
            val song = tracks.firstOrNull { it.id == songId } ?: tracks.first()
            _uiState.value = _uiState.value.copy(
                songs = tracks,
                currentSong = song,
                isPlaying = true,
                progressSeconds = 0
            )
        }
    }

    fun togglePlayPause() {
        _uiState.value = _uiState.value.copy(isPlaying = !_uiState.value.isPlaying)
    }

    fun playNext() {
        val current = _uiState.value.currentSong ?: return
        val currentIndex = _uiState.value.songs.indexOfFirst { it.id == current.id }
        val nextSong = _uiState.value.songs[(currentIndex + 1).floorMod(_uiState.value.songs.size)]
        _uiState.value = _uiState.value.copy(currentSong = nextSong, progressSeconds = 0, isPlaying = true)
    }

    fun playPrevious() {
        val current = _uiState.value.currentSong ?: return
        val currentIndex = _uiState.value.songs.indexOfFirst { it.id == current.id }
        val previousSong = _uiState.value.songs[(currentIndex - 1).floorMod(_uiState.value.songs.size)]
        _uiState.value = _uiState.value.copy(currentSong = previousSong, progressSeconds = 0, isPlaying = true)
    }

    fun toggleFavorite() {
        _uiState.value = _uiState.value.copy(isFavorite = !_uiState.value.isFavorite)
    }

    fun toggleShuffle() {
        _uiState.value = _uiState.value.copy(isShuffleEnabled = !_uiState.value.isShuffleEnabled)
    }

    fun toggleRepeat() {
        _uiState.value = _uiState.value.copy(isRepeatEnabled = !_uiState.value.isRepeatEnabled)
    }

    fun seekTo(progressSeconds: Int) {
        val duration = _uiState.value.currentSong?.durationSeconds ?: 0
        _uiState.value = _uiState.value.copy(progressSeconds = progressSeconds.coerceIn(0, duration))
    }

    fun tickProgress() {
        val duration = _uiState.value.currentSong?.durationSeconds ?: return
        if (!_uiState.value.isPlaying) return

        if (_uiState.value.progressSeconds >= duration) {
            if (_uiState.value.isRepeatEnabled) {
                _uiState.value = _uiState.value.copy(progressSeconds = 0)
            } else {
                playNext()
            }
        } else {
            _uiState.value = _uiState.value.copy(progressSeconds = _uiState.value.progressSeconds + 1)
        }
    }
}

private fun Int.floorMod(other: Int): Int = ((this % other) + other) % other
