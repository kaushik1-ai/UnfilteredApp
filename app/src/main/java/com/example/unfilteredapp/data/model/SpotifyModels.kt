package com.example.unfilteredapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyTokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)

@Serializable
data class SpotifySearchResponse(
    val tracks: SpotifyTracks
)

@Serializable
data class SpotifyTracks(
    val items: List<SpotifyTrack>
)

@Serializable
data class SpotifyTrack(
    val id: String,
    val name: String,
    val artists: List<SpotifyArtist>,
    val album: SpotifyAlbum,
    val preview_url: String? = null,
    val external_urls: SpotifyExternalUrls
)

@Serializable
data class SpotifyArtist(
    val name: String
)

@Serializable
data class SpotifyAlbum(
    val name: String,
    val images: List<SpotifyImage>
)

@Serializable
data class SpotifyImage(
    val url: String,
    val height: Int,
    val width: Int
)

@Serializable
data class SpotifyExternalUrls(
    val spotify: String
)
