package com.example.unfilteredapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class JournalEntryRequest(
    val content: String
)

@Serializable
data class JournalEntryResponse(
    val id: Int,
    val content: String,
    val created_at: String
)
