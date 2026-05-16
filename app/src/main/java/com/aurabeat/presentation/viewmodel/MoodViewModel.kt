package com.aurabeat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurabeat.presentation.ui.model.MoodPlaylist
import com.aurabeat.presentation.ui.model.MoodPlaylistGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private val defaultLoadingMessages = listOf(
    "Analyzing your mood...",
    "Finding matching vibes...",
    "Generating your soundtrack...",
    "Building your playlist..."
)

data class MoodUiState(
    val prompt: String = "",
    val isGenerating: Boolean = false,
    val loadingMessageIndex: Int = 0,
    val loadingProgress: Float = 0f,
    val playlist: MoodPlaylist = MoodPlaylistGenerator.allPlaylists().first(),
    val isSaved: Boolean = false
) {
    val loadingMessage: String
        get() = defaultLoadingMessages[loadingMessageIndex % defaultLoadingMessages.size]
}

class MoodViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MoodUiState())
    val uiState: StateFlow<MoodUiState> = _uiState

    fun startGeneration(prompt: String) {
        viewModelScope.launch {
            val nextPlaylist = MoodPlaylistGenerator.generate(prompt, _uiState.value.playlist.id)
            _uiState.value = _uiState.value.copy(
                prompt = prompt,
                isGenerating = true,
                loadingMessageIndex = 0,
                loadingProgress = 0f,
                playlist = nextPlaylist,
                isSaved = false
            )
        }
    }

    fun updateLoading(messageIndex: Int, progress: Float) {
        _uiState.value = _uiState.value.copy(
            loadingMessageIndex = messageIndex,
            loadingProgress = progress.coerceIn(0f, 1f)
        )
    }

    fun completeGeneration() {
        _uiState.value = _uiState.value.copy(isGenerating = false, loadingProgress = 1f)
    }

    fun regenerate() {
        viewModelScope.launch {
            val prompt = _uiState.value.prompt.ifBlank { "Surprise me" }
            val nextPlaylist = MoodPlaylistGenerator.generate(prompt, _uiState.value.playlist.id)
            _uiState.value = _uiState.value.copy(
                playlist = nextPlaylist,
                isSaved = false
            )
        }
    }

    fun toggleSaved() {
        _uiState.value = _uiState.value.copy(isSaved = !_uiState.value.isSaved)
    }
}

