package com.example.unfilteredapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: Int,
    val name: String,
    val mood_tag: String,
    val description: String? = null
)

@Serializable
data class Message(
    val id: Int? = null,
    
    @SerialName("room_id")
    val room_id: Int? = null,
    
    @SerialName("user_id")
    val user_id_snake: Int? = null,
    @SerialName("userId")
    val user_id_camel: Int? = null,

    @SerialName("user_name")
    val user_name_snake: String? = null,
    @SerialName("userName")
    val user_name_camel: String? = null,
    
    val content: String,
    
    @SerialName("created_at")
    val created_at: String? = null
) {
    // Helper properties to get the value regardless of case
    val effectiveUserId: Int get() = user_id_snake ?: user_id_camel ?: 0
    val effectiveUserName: String get() = "Anonymous"
    val effectiveRoomId: Int get() = room_id ?: 0
}
