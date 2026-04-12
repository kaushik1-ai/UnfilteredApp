package com.example.unfilteredapp.data.api

import com.example.unfilteredapp.data.model.JournalEntryRequest
import com.example.unfilteredapp.data.model.JournalEntryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JournalApi {
    @POST("api/journal/entries")
    suspend fun saveEntry(@Body request: JournalEntryRequest): Response<JournalEntryResponse>

    @GET("api/journal/entries")
    suspend fun getEntries(): Response<List<JournalEntryResponse>>
}
