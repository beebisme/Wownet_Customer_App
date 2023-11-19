package com.example.wowrackcustomerapp.data.models

data class ChatMessage(
    val id : Int,
    val senderId : String,
    val receiverId : String,
    val message : String,
)
