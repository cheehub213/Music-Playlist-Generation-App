package com.aurabeat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurabeat.core.Resource
import com.aurabeat.data.repository.AuthRepository
import com.aurabeat.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
	val isLoading: Boolean = false,
	val isAuthenticated: Boolean = false,
	val currentUser: User? = null,
	val sessionToken: String? = null,
	val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
	private val authRepository: AuthRepository
) : ViewModel() {
	private val _uiState = MutableStateFlow(AuthUiState())
	val uiState: StateFlow<AuthUiState> = _uiState

	init {
		// Check if already logged in
		val token = authRepository.getToken()
		if (token != null) {
			_uiState.value = _uiState.value.copy(
				isAuthenticated = true,
				sessionToken = token
			)
		}
	}

	fun login(email: String, password: String) {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true, error = null)
			try {
				val response = authRepository.loginAndGetResponse(email, password)
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					isAuthenticated = true,
                    currentUser = User(
                        id = response.user.id,
                        name = response.user.name ?: "User",
                        email = response.user.email
                    ),
					sessionToken = response.token
				)
			} catch (throwable: Throwable) {
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					error = throwable.message ?: "Login failed"
				)
			}
		}
	}

	fun register(name: String, email: String, password: String) {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true, error = null)
			try {
				val response = authRepository.register(name, email, password)
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					isAuthenticated = true,
					currentUser = User(
						id = response.user.id,
						email = response.user.email,
                        name = response.user.name ?: "User",
					),
					sessionToken = response.token
				)
			} catch (throwable: Throwable) {
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					error = throwable.message ?: "Registration failed"
				)
			}
		}
	}

	fun logout() {
		authRepository.logout()
		_uiState.value = AuthUiState()
	}

	fun clearError() {
		_uiState.value = _uiState.value.copy(error = null)
	}
}

