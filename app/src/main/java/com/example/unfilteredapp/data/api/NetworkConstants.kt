package com.example.unfilteredapp.data.api
 
import com.example.unfilteredapp.BuildConfig

object NetworkConstants {
    const val BASE_URL = "https://unfilteredapp-backend.onrender.com/"
    
    // For Socket.io we need the URL without the trailing slash
    val SOCKET_URL = BASE_URL.removeSuffix("/")
    const val GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com/maps/api/"
    val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
}
