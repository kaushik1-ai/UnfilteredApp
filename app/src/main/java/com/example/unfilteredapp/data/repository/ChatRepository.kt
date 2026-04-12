package com.example.unfilteredapp.data.repository

import android.content.Context
import com.example.unfilteredapp.data.api.ChatApi
import com.example.unfilteredapp.data.api.NetworkConstants
import com.example.unfilteredapp.data.model.Message
import com.example.unfilteredapp.data.model.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit

class ChatRepository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    private val authInterceptor = Interceptor { chain ->
        val token = sharedPreferences.getString("jwt_token", "")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    private val chatApi = retrofit.create(ChatApi::class.java)

    suspend fun getRooms(): Response<List<Room>> {
        return chatApi.getRooms()
    }

    suspend fun getMessages(roomId: Int): Response<List<Message>> {
        return chatApi.getMessages(roomId)
    }
}
