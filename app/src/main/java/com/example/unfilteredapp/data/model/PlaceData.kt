package com.example.unfilteredapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlacesResponse(
    val results: List<PlaceResult>,
    val status: String,
    val error_message: String? = null
)

@Serializable
data class PlaceResult(
    val name: String,
    val vicinity: String? = null,
    val geometry: PlaceGeometry,
    val place_id: String,
    val types: List<String> = emptyList()
)

@Serializable
data class PlaceGeometry(
    val location: PlaceLocation
)

@Serializable
data class PlaceLocation(
    val lat: Double,
    val lng: Double
)
