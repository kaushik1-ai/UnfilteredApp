package com.example.unfilteredapp.data.api

import com.example.unfilteredapp.data.model.SpotifySearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApi {
    @GET("v1/search")
    suspend fun searchTracks(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 10
    ): Response<SpotifySearchResponse>

    @GET("v1/recommendations")
    suspend fun getRecommendations(
        @Header("Authorization") token: String,
        @Query("seed_genres") genres: String,
        @Query("limit") limit: Int = 10
    ): Response<SpotifySearchResponse>
}
