package com.example.unfilteredapp.data.repository

import android.util.Base64
import com.example.unfilteredapp.BuildConfig
import com.example.unfilteredapp.data.api.SpotifyApi
import com.example.unfilteredapp.data.api.SpotifyAuthApi
import com.example.unfilteredapp.data.model.SpotifyTrack
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class SpotifyRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val authRetrofit = Retrofit.Builder()
        .baseUrl("https://accounts.spotify.com/")
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    private val apiRetrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    private val authApi = authRetrofit.create(SpotifyAuthApi::class.java)
    private val spotifyApi = apiRetrofit.create(SpotifyApi::class.java)

    private var cachedToken: String? = null
    private var tokenExpiresAt: Long = 0

    private suspend fun getAccessToken(): String? {
        if (cachedToken != null && System.currentTimeMillis() < tokenExpiresAt) {
            return cachedToken
        }

        val authString = "${BuildConfig.SPOTIFY_CLIENT_ID}:${BuildConfig.SPOTIFY_CLIENT_SECRET}"
        val basicAuth = "Basic " + Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)

        try {
            val response = authApi.getAccessToken(basicAuth)
            if (response.isSuccessful) {
                val tokenResponse = response.body()
                cachedToken = tokenResponse?.access_token
                tokenExpiresAt = System.currentTimeMillis() + ((tokenResponse?.expires_in ?: 0) * 1000)
                return cachedToken
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun getMoodSuggestions(mood: String): List<SpotifyTrack> {
        val token = getAccessToken() ?: return emptyList()
        val bearerToken = "Bearer $token"

        // Map moods to Spotify search terms or genres
        val query = when (mood.lowercase()) {
            "happy", "excited" -> "feel good"
            "sad", "lonely" -> "melancholy"
            "calm", "peaceful" -> "ambient chill"
            "frustrated", "angry" -> "energetic rock"
            "anxious" -> "calming nature"
            else -> mood
        }

        return try {
            val response = spotifyApi.searchTracks(bearerToken, query)
            if (response.isSuccessful) {
                response.body()?.tracks?.items ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
