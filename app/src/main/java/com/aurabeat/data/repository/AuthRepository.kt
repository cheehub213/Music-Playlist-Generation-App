package com.aurabeat.data.repository

import com.aurabeat.data.remote.AuraBeatApiService
import com.aurabeat.data.remote.dto.LoginRequest
import com.aurabeat.domain.repository.IAuthRepository

class AuthRepository(
    private val api: AuraBeatApiService
) : IAuthRepository {
    override suspend fun login(email: String, password: String): String {
        return api.login(LoginRequest(email, password))
    }
}
