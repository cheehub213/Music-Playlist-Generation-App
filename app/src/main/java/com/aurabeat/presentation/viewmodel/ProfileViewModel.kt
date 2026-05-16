package com.aurabeat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurabeat.core.Resource
import com.aurabeat.data.repository.FakeUserRepository
import com.aurabeat.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val selectedGenres: Set<String> = setOf("Lo-fi", "Jazz", "Chill", "Ambient"),
    val error: String? = null
)

class ProfileViewModel(
    private val userRepository: FakeUserRepository = FakeUserRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = userRepository.getCurrentUser()) {
                is Resource.Success<*> -> _uiState.value = _uiState.value.copy(isLoading = false, user = result.data as User)
                is Resource.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = result.message ?: "Failed to load profile")
                else -> _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun toggleGenre(genre: String) {
        val current = _uiState.value.selectedGenres
        _uiState.value = _uiState.value.copy(
            selectedGenres = if (current.contains(genre)) current - genre else current + genre
        )
    }
}
