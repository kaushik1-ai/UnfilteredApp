package com.example.unfilteredapp.data.api

object NetworkConstants {
    const val BASE_URL = "http://10.0.2.2:3000/"
    
    // For Socket.io we need the URL without the trailing slash
    val SOCKET_URL = BASE_URL.removeSuffix("/")
}
