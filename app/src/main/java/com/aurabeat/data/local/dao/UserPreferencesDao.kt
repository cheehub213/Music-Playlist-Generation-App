package com.aurabeat.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.aurabeat.data.local.entity.UserPreferencesEntity

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences")
    suspend fun getUserPreferences(): List<UserPreferencesEntity>
}
