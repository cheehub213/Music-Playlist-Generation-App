package com.aurabeat.data.backend

import com.aurabeat.data.repository.FakeDataProvider
import com.aurabeat.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * In-memory music playback simulation. The player viewmodel can bind to this now and a real player later.
 */
object FakePlaybackManager {
    data class PlaybackState(
        val queue: List<Song> = FakeDataProvider.sampleSongs(),
        val currentSong: Song? = FakeDataProvider.sampleSongs().firstOrNull(),
        val isPlaying: Boolean = false,
        val isShuffleEnabled: Boolean = false,
        val isRepeatEnabled: Boolean = false,
        val progressSeconds: Int = 0,
        val isFavorite: Boolean = false
    )

    private val playbackFlow = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = playbackFlow

    fun playQueue(queue: List<Song>, startSongId: String? = null) {
        if (queue.isEmpty()) return
        val startSong = queue.firstOrNull { it.id == startSongId } ?: queue.first()
        playbackFlow.value = playbackFlow.value.copy(
            queue = queue,
            currentSong = startSong,
            isPlaying = true,
            progressSeconds = 0
        )
    }

    fun playSong(songId: String) {
        val queue = playbackFlow.value.queue.ifEmpty { FakeDataProvider.sampleSongs() }
        val song = queue.firstOrNull { it.id == songId } ?: queue.firstOrNull() ?: return
        playbackFlow.value = playbackFlow.value.copy(currentSong = song, isPlaying = true, progressSeconds = 0)
    }

    fun togglePlayPause() {
        playbackFlow.value = playbackFlow.value.copy(isPlaying = !playbackFlow.value.isPlaying)
    }

    fun toggleShuffle() {
        playbackFlow.value = playbackFlow.value.copy(isShuffleEnabled = !playbackFlow.value.isShuffleEnabled)
    }

    fun toggleRepeat() {
        playbackFlow.value = playbackFlow.value.copy(isRepeatEnabled = !playbackFlow.value.isRepeatEnabled)
    }

    fun toggleFavorite() {
        playbackFlow.value = playbackFlow.value.copy(isFavorite = !playbackFlow.value.isFavorite)
    }

    fun seekTo(progressSeconds: Int) {
        val duration = playbackFlow.value.currentSong?.durationSeconds ?: 0
        playbackFlow.value = playbackFlow.value.copy(progressSeconds = progressSeconds.coerceIn(0, duration))
    }

    fun tickProgress() {
        val current = playbackFlow.value.currentSong ?: return
        if (!playbackFlow.value.isPlaying) return
        val nextProgress = playbackFlow.value.progressSeconds + 1
        playbackFlow.value = when {
            nextProgress >= current.durationSeconds && playbackFlow.value.isRepeatEnabled ->
                playbackFlow.value.copy(progressSeconds = 0)
            nextProgress >= current.durationSeconds -> {
                val currentIndex = playbackFlow.value.queue.indexOfFirst { it.id == current.id }
                val nextSong = playbackFlow.value.queue[(currentIndex + 1).floorMod(playbackFlow.value.queue.size)]
                playbackFlow.value.copy(currentSong = nextSong, progressSeconds = 0)
            }
            else -> playbackFlow.value.copy(progressSeconds = nextProgress)
        }
    }
}

private fun Int.floorMod(other: Int): Int = ((this % other) + other) % other
