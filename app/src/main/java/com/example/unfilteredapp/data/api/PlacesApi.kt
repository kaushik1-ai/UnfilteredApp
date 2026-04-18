package com.example.unfilteredapp.data.api

import com.example.unfilteredapp.data.model.PlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String, // "lat,lng"
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") apiKey: String
    ): Response<PlacesResponse>
}
