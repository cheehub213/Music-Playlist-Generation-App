package com.aurabeat.domain.usecase

import com.aurabeat.domain.repository.IPlaylistRepository

class GetPlaylistsUseCase(
    private val repository: IPlaylistRepository
) {
    suspend operator fun invoke() = repository.getPlaylists()
}
