package com.aurabeat.data.remote.dto

data class LoginResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: String,
    val email: String,
    val name: String? = null,
    val created_at: String? = null
)
