package com.aurabeat.data.remote

import com.aurabeat.data.remote.dto.LoginRequest
import com.aurabeat.data.remote.dto.PlaylistResponse
import com.aurabeat.data.remote.dto.TrackResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuraBeatApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): String

    @GET("playlists")
    suspend fun getPlaylists(): List<PlaylistResponse>

    @GET("tracks")
    suspend fun getTracks(): List<TrackResponse>
}
