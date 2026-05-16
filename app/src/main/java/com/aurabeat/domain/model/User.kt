package com.aurabeat.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String?,
    val avatarUrl: String = "",
    val isPremium: Boolean = false,
    val favoriteGenres: List<String> = emptyList()
)
