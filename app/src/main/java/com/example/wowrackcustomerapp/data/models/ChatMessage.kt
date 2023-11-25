package com.example.wowrackcustomerapp.data.models

import java.util.Date

data class ChatMessage(
    val id : Int? = null,
    var senderId : String? = null,
    var receiverId : String? = null,
    var message : String? = null,
    var dateObject: Date? = null
)
