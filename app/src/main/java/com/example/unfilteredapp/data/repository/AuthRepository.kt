package com.example.unfilteredapp.data.repository

import android.content.Context
import com.example.unfilteredapp.data.api.AuthApi
import com.example.unfilteredapp.data.api.NetworkConstants
import com.example.unfilteredapp.data.model.AuthRequest
import com.example.unfilteredapp.data.model.AuthResponse
import com.example.unfilteredapp.data.model.RegistrationRequest
import com.example.unfilteredapp.data.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit

class AuthRepository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    private val authInterceptor = Interceptor { chain ->
        val token = sharedPreferences.getString("jwt_token", "")
        val request = chain.request().newBuilder()
            .apply {
                if (token?.isNotEmpty() == true) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()
        chain.proceed(request)
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    private val authApi = retrofit.create(AuthApi::class.java)

    suspend fun register(request: RegistrationRequest): Response<AuthResponse> {
        return authApi.register(request)
    }

    suspend fun login(request: AuthRequest): Response<AuthResponse> {
        return authApi.login(request)
    }

    suspend fun getMe(): Response<User> {
        return authApi.getMe()
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun clearToken() {
        sharedPreferences.edit().clear().apply()
    }
}
