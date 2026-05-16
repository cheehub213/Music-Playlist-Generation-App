package com.aurabeat.data.repository

import com.aurabeat.data.backend.FakeAuthManager
import com.aurabeat.domain.repository.IAuthRepository

/**
 * Fake auth repository that simulates API calls and keeps an in-memory session.
 * Real auth later can replace this class without affecting the UI layer.
 */
class FakeAuthRepository : IAuthRepository {
    override suspend fun login(email: String, password: String): String {
        return FakeAuthManager.login(email, password).let { result ->
            when (result) {
                is com.aurabeat.core.Resource.Success -> FakeAuthManager.restoreSession().sessionToken.orEmpty()
                is com.aurabeat.core.Resource.Error -> throw IllegalStateException(result.message ?: "Login failed")
                com.aurabeat.core.Resource.Loading -> ""
            }
        }
    }

    suspend fun signup(name: String, email: String, password: String) = FakeAuthManager.signup(name, email, password)
    fun logout() = FakeAuthManager.logout()
    fun sessionState() = FakeAuthManager.sessionState
}
