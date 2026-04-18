package com.example.unfilteredapp.data.repository

import com.example.unfilteredapp.data.api.NetworkConstants
import com.example.unfilteredapp.data.api.PlacesApi
import com.example.unfilteredapp.data.model.PlacesResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit

class PlacesRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.GOOGLE_MAPS_BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    private val placesApi = retrofit.create(PlacesApi::class.java)

    suspend fun getNearbyPlaces(lat: Double, lng: Double, type: String): Response<PlacesResponse> {
        val locationStr = "$lat,$lng"
        // Map user labels to Google Place types
        val googleType = when(type.lowercase()) {
            "restaurants" -> "restaurant"
            "parks" -> "park"
            "shopping" -> "shopping_mall"
            "cafes" -> "cafe"
            "gyms" -> "gym"
            "hospitals" -> "hospital"
            else -> type.lowercase()
        }
        
        return placesApi.getNearbyPlaces(
            location = locationStr,
            radius = 3000, // 3km radius
            type = googleType,
            apiKey = NetworkConstants.MAPS_API_KEY
        )
    }
}
