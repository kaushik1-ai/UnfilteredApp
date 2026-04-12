package com.example.unfilteredapp.data.api

import com.example.unfilteredapp.data.model.AuthRequest
import com.example.unfilteredapp.data.model.AuthResponse
import com.example.unfilteredapp.data.model.RegistrationRequest
import com.example.unfilteredapp.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegistrationRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @GET("api/auth/me")
    suspend fun getMe(): Response<User>
}
