package com.aurabeat.domain.usecase

import com.aurabeat.domain.model.Playlist

class CreatePlaylistUseCase {
    suspend operator fun invoke(name: String): Playlist {
        return Playlist(
            id = "local",
            title = name,
            description = "Locally generated playlist",
            tracks = emptyList()
        )
    }
}
