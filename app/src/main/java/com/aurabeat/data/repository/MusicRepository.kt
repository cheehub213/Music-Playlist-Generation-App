package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.domain.model.Song

interface MusicRepository {
    suspend fun getTopSongs(): Resource<List<Song>>
    suspend fun getSongById(id: String): Resource<Song>
}
