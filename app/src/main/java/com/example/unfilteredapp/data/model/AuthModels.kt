package com.example.unfilteredapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val otp: String
)

@Serializable
data class RegistrationRequest(
    val name: String,
    val email: String
)

@Serializable
data class AuthResponse(
    val token: String, // Made non-nullable
    val message: String? = null,
    val user: User
)

@Serializable
data class User(
    val id: Int,
    val email: String,
    val name: String? = null
)
