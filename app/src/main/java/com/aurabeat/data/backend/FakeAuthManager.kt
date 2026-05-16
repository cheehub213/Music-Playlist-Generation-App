package com.aurabeat.data.backend

import com.aurabeat.core.Resource
import com.aurabeat.data.repository.FakeDataProvider
import com.aurabeat.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * In-memory auth/session store used to simulate login, signup, logout, and session restore.
 */
object FakeAuthManager {
    data class AuthState(
        val isLoading: Boolean = false,
        val isAuthenticated: Boolean = false,
        val currentUser: User? = null,
        val sessionToken: String? = null,
        val error: String? = null
    )

    private val sessionFlow = MutableStateFlow(AuthState())
    val sessionState: StateFlow<AuthState> = sessionFlow

    suspend fun login(email: String, password: String): Resource<User> {
        return FakeBackendSimulator.request("Login", minDelayMs = 250L, maxDelayMs = 700L, failureRate = 0.08f) {
            val user = FakeDataProvider.sampleUser().copy(
                email = email,
                name = email.substringBefore('@').replaceFirstChar { it.uppercase() }
            )
            sessionFlow.value = sessionFlow.value.copy(
                isAuthenticated = true,
                currentUser = user,
                sessionToken = generateToken(email),
                error = null
            )
            user
        }
    }

    suspend fun signup(name: String, email: String, password: String): Resource<User> {
        return FakeBackendSimulator.request("Signup", minDelayMs = 350L, maxDelayMs = 900L, failureRate = 0.1f) {
            val user = FakeDataProvider.sampleUser().copy(
                name = name,
                email = email,
                favoriteGenres = listOf("Chill", "Ambient", "Pop")
            )
            sessionFlow.value = sessionFlow.value.copy(
                isAuthenticated = true,
                currentUser = user,
                sessionToken = generateToken(email),
                error = null
            )
            user
        }
    }

    fun logout() {
        sessionFlow.value = AuthState()
    }

    fun restoreSession(): AuthState = sessionFlow.value

    private fun generateToken(seed: String): String =
        "aurabeat-${seed.hashCode().toString(16)}"
}
