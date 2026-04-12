package com.example.unfilteredapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MoodLogRequest(
    val modeType: String,
    val modeSubType: String
)

@Serializable
data class GenericResponse(
    val message: String
)

@Serializable
data class AnalyticsResponse(
    val totalLogs: Int,
    val moodCounts: List<MoodCount>,
    val dailyLogs: List<MoodLogEntry>
)

@Serializable
data class MoodCount(
    val modeType: String,
    val count: Int
)

@Serializable
data class MoodLogEntry(
    val date: String,
    val modeType: String,
    val modeSubType: String
)
