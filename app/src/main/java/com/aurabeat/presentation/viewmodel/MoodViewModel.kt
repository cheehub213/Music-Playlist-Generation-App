package com.aurabeat.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurabeat.data.remote.dto.Result
import com.aurabeat.data.repository.GroqDJRepository
import com.aurabeat.presentation.ui.model.MoodPlaylist
import com.aurabeat.presentation.ui.model.MoodPlaylistGenerator
import com.aurabeat.presentation.ui.model.MoodTrack
import com.aurabeat.presentation.ui.model.MoodAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MoodViewModel"

private val defaultLoadingMessages = listOf(
    "Analyzing your vibe...",
    "Consulting the DJ AI...",
    "Crafting your mix...",
    "Building your playlist..."
)

data class MoodUiState(
    val prompt: String = "",
    val isGenerating: Boolean = false,
    val loadingMessageIndex: Int = 0,
    val loadingProgress: Float = 0f,
    val playlist: MoodPlaylist = MoodPlaylistGenerator.allPlaylists().first(),
    val isSaved: Boolean = false,
    val error: String? = null
) {
    val loadingMessage: String
        get() = defaultLoadingMessages[loadingMessageIndex % defaultLoadingMessages.size]
}

@HiltViewModel
class MoodViewModel @Inject constructor(
    private val groqDJRepository: GroqDJRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MoodUiState())
    val uiState: StateFlow<MoodUiState> = _uiState

    fun startGeneration(prompt: String) {
        Log.d(TAG, "startGeneration called with prompt: $prompt")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                prompt = prompt,
                isGenerating = true,
                loadingMessageIndex = 0,
                loadingProgress = 0f,
                isSaved = false,
                error = null
            )
            
            try {
                Log.d(TAG, "Calling Groq DJ API")
                val result = groqDJRepository.generatePlaylistFromGroqDJ(prompt)
                
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "Groq DJ success: ${result.data.playlist.tracks.size} tracks")
                        val moodPlaylist = convertToMoodPlaylist(result.data)
                        _uiState.value = _uiState.value.copy(
                            playlist = moodPlaylist,
                            error = null,
                            isGenerating = false
                        )
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Groq DJ error: ${result.exception.message}")
                        _uiState.value = _uiState.value.copy(
                            error = result.exception.message ?: "Failed to generate playlist",
                            isGenerating = false
                        )
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "Loading state received")
                    }
                }
            } catch (throwable: Throwable) {
                Log.e(TAG, "Unexpected error", throwable)
                _uiState.value = _uiState.value.copy(
                    error = throwable.message ?: "Failed to generate playlist",
                    isGenerating = false
                )
            }
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
        val prompt = _uiState.value.prompt.ifBlank { "Surprise me" }
        startGeneration(prompt)
    }

    fun toggleSaved() {
        _uiState.value = _uiState.value.copy(isSaved = !_uiState.value.isSaved)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun convertToMoodPlaylist(response: com.aurabeat.data.remote.dto.GroqDJPlaylistResponse): MoodPlaylist {
        val tracks = response.playlist.tracks.map { track ->
            MoodTrack(
                id = track.id,
                title = track.title,
                artist = track.artist,
                durationSeconds = (track.durationMs ?: 0) / 1000,
                artworkColor = 0xFF7C4DFF
            )
        }
        
        return MoodPlaylist(
            id = response.playlist.id,
            title = response.playlist.title,
            description = response.playlist.description,
            moodTags = listOf(response.inputPrompt),
            analysis = MoodAnalysis(
                mood = response.inputPrompt,
                energy = "High",
                vibe = "Groq DJ Curated",
                genres = emptyList(),
                keywords = listOf(response.inputPrompt)
            ),
            coverColor = 0xFF7C4DFF,
            tracks = tracks
        )
    }
}


