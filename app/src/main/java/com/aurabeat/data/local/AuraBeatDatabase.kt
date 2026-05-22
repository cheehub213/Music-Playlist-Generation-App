package com.aurabeat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aurabeat.data.local.dao.PlaylistDao
import com.aurabeat.data.local.dao.TrackDao
import com.aurabeat.data.local.dao.UserPreferencesDao
import com.aurabeat.data.local.entity.PlaylistEntity
import com.aurabeat.data.local.entity.TrackEntity
import com.aurabeat.data.local.entity.UserPreferencesEntity

@Database(
    entities = [PlaylistEntity::class, TrackEntity::class, UserPreferencesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AuraBeatDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackDao(): TrackDao
    abstract fun userPreferencesDao(): UserPreferencesDao
}
