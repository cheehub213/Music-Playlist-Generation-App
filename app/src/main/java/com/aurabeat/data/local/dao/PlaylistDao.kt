package com.aurabeat.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.aurabeat.data.local.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylists(): List<PlaylistEntity>
}
