package com.aurabeat.data.repository

import com.aurabeat.core.Resource
import com.aurabeat.domain.model.User

interface UserRepository {
	suspend fun getCurrentUser(): Resource<User>
}
