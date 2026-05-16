package com.aurabeat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurabeat.core.Resource
import com.aurabeat.data.repository.FakeAuthRepository
import com.aurabeat.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
	val isLoading: Boolean = false,
	val isAuthenticated: Boolean = false,
	val currentUser: User? = null,
	val sessionToken: String? = null,
	val error: String? = null
)

class AuthViewModel(
	private val authRepository: FakeAuthRepository = FakeAuthRepository()
) : ViewModel() {
	private val _uiState = MutableStateFlow(AuthUiState())
	val uiState: StateFlow<AuthUiState> = _uiState

	fun login(email: String, password: String) {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true, error = null)
			try {
				val token = authRepository.login(email, password)
				val session = authRepository.sessionState().value
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					isAuthenticated = true,
					currentUser = session.currentUser,
					sessionToken = token
				)
			} catch (throwable: Throwable) {
				_uiState.value = _uiState.value.copy(isLoading = false, error = throwable.message ?: "Login failed")
			}
		}
	}

	fun signup(name: String, email: String, password: String) {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true, error = null)
			when (val result = authRepository.signup(name, email, password)) {
				is Resource.Success -> {
					_uiState.value = _uiState.value.copy(
						isLoading = false,
						isAuthenticated = true,
						currentUser = result.data,
						sessionToken = authRepository.sessionState().value.sessionToken
					)
				}
				is Resource.Error -> {
					_uiState.value = _uiState.value.copy(isLoading = false, error = result.message ?: "Signup failed")
				}
				Resource.Loading -> Unit
			}
		}
	}

	fun logout() {
		authRepository.logout()
		_uiState.value = AuthUiState()
	}
}
