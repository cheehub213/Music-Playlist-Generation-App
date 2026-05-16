package com.aurabeat.domain.repository

interface IAuthRepository {
    suspend fun login(email: String, password: String): String
}
