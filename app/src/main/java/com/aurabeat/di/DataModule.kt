package com.aurabeat.di

import com.aurabeat.data.local.AuraBeatDatabase
import com.aurabeat.data.local.dao.PlaylistDao
import com.aurabeat.data.local.dao.TrackDao
import com.aurabeat.data.local.dao.UserPreferencesDao
import com.aurabeat.data.remote.AuraBeatApiService
import com.aurabeat.data.repository.GroqDJRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providePlaylistDao(database: AuraBeatDatabase): PlaylistDao = database.playlistDao()

    @Provides
    @Singleton
    fun provideTrackDao(database: AuraBeatDatabase): TrackDao = database.trackDao()

    @Provides
    @Singleton
    fun provideUserPreferencesDao(database: AuraBeatDatabase): UserPreferencesDao = database.userPreferencesDao()

    @Provides
    @Singleton
    fun provideGroqDJRepository(apiService: AuraBeatApiService): GroqDJRepository {
        return GroqDJRepository(apiService)
    }
}
