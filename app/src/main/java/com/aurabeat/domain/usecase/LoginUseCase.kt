package com.aurabeat.domain.usecase

import com.aurabeat.domain.repository.IAuthRepository

class LoginUseCase(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): String {
        return repository.login(email, password)
    }
}
