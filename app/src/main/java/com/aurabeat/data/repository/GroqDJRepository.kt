package com.aurabeat.data.repository

import android.util.Log
import com.aurabeat.data.remote.AuraBeatApiService
import com.aurabeat.data.remote.dto.GroqDJRequest
import com.aurabeat.data.remote.dto.GroqDJPlaylistResponse
import com.aurabeat.data.remote.dto.Result
import javax.inject.Inject

class GroqDJRepository @Inject constructor(
    private val apiService: AuraBeatApiService
) {
    suspend fun generatePlaylistFromGroqDJ(prompt: String): Result<GroqDJPlaylistResponse> {
        return try {
            if (prompt.isBlank()) {
                return Result.Error(
                    IllegalArgumentException("Prompt cannot be empty")
                )
            }

            Log.d(TAG, "Requesting Groq DJ for prompt: $prompt")
            
            val response = apiService.generatePlaylistFromGroqDJ(
                GroqDJRequest(prompt = prompt.trim())
            )
            
            // Validate response
            if (response.playlist.tracks.isEmpty()) {
                return Result.Error(
                    Exception("No tracks returned from Groq DJ")
                )
            }

            Log.d(TAG, "Groq DJ success: ${response.playlist.tracks.size} tracks")
            Result.Success(response)
            
        } catch (e: retrofit2.HttpException) {
            Log.e(TAG, "HTTP Error: ${e.code()} - ${e.message()}", e)
            val errorMsg = when (e.code()) {
                400 -> "Invalid request to Groq DJ service"
                401 -> "Authentication failed"
                403 -> "Access denied to Groq DJ service"
                404 -> "Groq DJ service not found"
                429 -> "Rate limit exceeded. Please try again later"
                504 -> "Service timeout. The AI took too long to respond"
                else -> "HTTP Error: ${e.code()}"
            }
            Result.Error(Exception(errorMsg))
            
        } catch (e: java.net.SocketTimeoutException) {
            Log.e(TAG, "Network timeout", e)
            Result.Error(
                Exception("Network timeout. Please check your connection and try again")
            )
            
        } catch (e: java.net.UnknownHostException) {
            Log.e(TAG, "Cannot reach server", e)
            Result.Error(
                Exception("Cannot reach the server. Check your internet connection")
            )
            
        } catch (e: com.google.gson.JsonSyntaxException) {
            Log.e(TAG, "Invalid JSON response", e)
            Result.Error(
                Exception("Server returned invalid data format")
            )
            
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            Result.Error(
                Exception("Unexpected error: ${e.message ?: "Unknown error"}")
            )
        }
    }

    companion object {
        private const val TAG = "GroqDJRepository"
    }
}
