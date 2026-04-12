package com.example.unfilteredapp.data.api

import com.example.unfilteredapp.data.model.Message
import com.example.unfilteredapp.data.model.Room
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatApi {
    @GET("api/rooms")
    suspend fun getRooms(): Response<List<Room>>

    @GET("api/rooms/{id}/messages")
    suspend fun getMessages(@Path("id") roomId: Int): Response<List<Message>>
}
