package com.example.unfilteredapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unfilteredapp.data.model.*
import com.example.unfilteredapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val token: String) : AuthState()
    data class Error(val message: String) : AuthState()
    object Unauthenticated : AuthState()
    
    // Legacy support for older screen logic
    object LoginSuccess : AuthState()
    object RegistrationSuccess : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        checkAutoLogin()
    }

    fun isLoggedIn(): Boolean {
        return repository.getToken() != null
    }

    private fun checkAutoLogin() {
        val token = repository.getToken()
        if (token != null) {
            _authState.value = AuthState.Authenticated(token)
            fetchCurrentUser()
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            try {
                val response = repository.getMe()
                if (response.isSuccessful) {
                    _currentUser.value = response.body()
                } else if (response.code() == 401) {
                    logout()
                }
            } catch (e: Exception) {
                // If network fails, keep session local for now
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.login(AuthRequest(email.trim(), password.trim()))
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse?.token != null) {
                        repository.saveToken(authResponse.token)
                        _currentUser.value = authResponse.user
                        _authState.value = AuthState.Authenticated(authResponse.token)
                    } else {
                        _authState.value = AuthState.Error("Login failed: No token received")
                    }
                } else {
                    _authState.value = AuthState.Error("Login failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.register(
                    RegistrationRequest(name.trim(), email.trim(), password.trim())
                )
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        // Registration success - doesn't return token, user must log in
                        _authState.value = AuthState.RegistrationSuccess
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _authState.value = AuthState.Error(errorBody ?: "Registration failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error")
            }
        }
    }

    fun logout() {
        repository.clearToken()
        _currentUser.value = null
        _authState.value = AuthState.Unauthenticated
    }

    fun resetState() {
        if (_authState.value !is AuthState.Authenticated && _authState.value !is AuthState.Unauthenticated) {
            _authState.value = AuthState.Idle
        }
    }
}
