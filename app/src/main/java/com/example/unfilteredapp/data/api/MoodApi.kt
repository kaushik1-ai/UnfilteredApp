package com.example.unfilteredapp.data.api

import com.example.unfilteredapp.data.model.AnalyticsResponse
import com.example.unfilteredapp.data.model.GenericResponse
import com.example.unfilteredapp.data.model.MoodLogRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MoodApi {
    @POST("api/mood/log")
    suspend fun logMood(@Body request: MoodLogRequest): Response<GenericResponse>

    @GET("api/mood/analytics")
    suspend fun getAnalytics(@Query("days") days: Int = 7): Response<AnalyticsResponse>
}
