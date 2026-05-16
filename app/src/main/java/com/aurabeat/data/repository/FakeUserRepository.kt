package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.data.backend.FakeAuthManager
import com.aurabeat.data.backend.FakeBackendSimulator
import com.aurabeat.domain.model.User

class FakeUserRepository : UserRepository {
    override suspend fun getCurrentUser(): Resource<User> {
        val session = FakeAuthManager.restoreSession().currentUser
        return FakeBackendSimulator.request("Current user", minDelayMs = 120L, maxDelayMs = 260L) {
            session ?: FakeDataProvider.sampleUser()
        }
    }
}
