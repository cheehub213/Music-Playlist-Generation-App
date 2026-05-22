package com.aurabeat.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.aurabeat.data.local.entity.TrackEntity

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks")
    suspend fun getAllTracks(): List<TrackEntity>
}
