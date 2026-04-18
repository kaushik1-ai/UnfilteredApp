package com.example.unfilteredapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegistrationRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String? = null,
    val message: String? = null,
    val user: User
)

@Serializable
data class User(
    val id: Int,
    val email: String,
    val name: String? = null
)
