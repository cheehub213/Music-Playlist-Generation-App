package com.aurabeat.core

import com.aurabeat.data.backend.FakeAuthManager
import com.aurabeat.data.backend.FakePlaybackManager
import com.aurabeat.data.backend.FakeSearchEngine
import com.aurabeat.data.repository.FakeArtistRepository
import com.aurabeat.data.repository.FakeAuthRepository
import com.aurabeat.data.repository.FakeMoodRepository
import com.aurabeat.data.repository.FakeMusicRepository
import com.aurabeat.data.repository.FakePlaylistRepository
import com.aurabeat.data.repository.FakeUserRepository

/**
 * Simple provider for app-wide dependencies. Replace with Hilt later.
 */
class AppContainer {
    val musicRepository = FakeMusicRepository()
    val playlistRepository = FakePlaylistRepository()
    val artistRepository = FakeArtistRepository()
    val moodRepository = FakeMoodRepository()
    val userRepository = FakeUserRepository()
    val authRepository = FakeAuthRepository()
    val searchEngine = FakeSearchEngine()
    val playbackManager = FakePlaybackManager
    val authSession = FakeAuthManager
}
