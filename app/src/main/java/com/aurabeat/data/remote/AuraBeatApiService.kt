package com.aurabeat.data.remote

import com.aurabeat.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuraBeatApiService {
    // Auth endpoints
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: LoginRequest): LoginResponse

    // Recommendations endpoints
    @POST("recommendations/groq-dj")
    suspend fun generatePlaylistFromGroqDJ(@Body request: GroqDJRequest): GroqDJPlaylistResponse

    @POST("recommendations/generate")
    suspend fun generatePlaylist(@Body request: MoodPromptRequest): PlaylistGenerationResponse

    @POST("recommendations/analyze")
    suspend fun analyzeMood(@Body request: MoodPromptRequest): MoodAnalysisResponse

    // Spotify search endpoints
    @GET("spotify/search")
    suspend fun searchTracks(@Query("q") query: String, @Query("limit") limit: Int = 20): SearchResponse

    @GET("spotify/featured-playlists")
    suspend fun getFeaturedPlaylists(): FeaturedPlaylistsResponse

    // Playlist endpoints
    @GET("playlists")
    suspend fun getUserPlaylists(): List<PlaylistResponse>

    @POST("playlists")
    suspend fun savePlaylist(@Body playlist: PlaylistRequest): PlaylistResponse

    // Track endpoints
    @GET("tracks")
    suspend fun getTracks(): List<TrackResponse>
}
