package com.aurabeat.data.repository

import android.content.SharedPreferences
import com.aurabeat.data.remote.AuraBeatApiService
import com.aurabeat.data.remote.dto.LoginRequest
import com.aurabeat.data.remote.dto.LoginResponse
import com.aurabeat.domain.repository.IAuthRepository
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuraBeatApiService,
    private val sharedPreferences: SharedPreferences
) : IAuthRepository {
    companion object {
        private const val TOKEN_KEY = "auth_token"
        private const val USER_ID_KEY = "user_id"
        private const val USER_EMAIL_KEY = "user_email"
    }

    override suspend fun login(email: String, password: String): String {
        val response = api.login(LoginRequest(email, password))
        saveToken(response)
        return response.token
    }

    suspend fun loginAndGetResponse(email: String, password: String): LoginResponse {
        val response = api.login(LoginRequest(email, password))
        saveToken(response)
        return response
    }

    suspend fun register(name: String, email: String, password: String): LoginResponse {
        val response = api.register(LoginRequest(email, password, name))
        saveToken(response)
        return response
    }

    private fun saveToken(response: LoginResponse) {
        sharedPreferences.edit().apply {
            putString(TOKEN_KEY, response.token)
            putString(USER_ID_KEY, response.user.id)
            putString(USER_EMAIL_KEY, response.user.email)
            apply()
        }
    }

    fun getToken(): String? = sharedPreferences.getString(TOKEN_KEY, null)

    fun getUserId(): String? = sharedPreferences.getString(USER_ID_KEY, null)

    fun getUserEmail(): String? = sharedPreferences.getString(USER_EMAIL_KEY, null)

    fun isLoggedIn(): Boolean = getToken() != null

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}
