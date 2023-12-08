package com.example.wowrackcustomerapp.data.models

data class UserModel(
    val userId : String,
    val name : String,
    val email : String,
    val password : String,
    val token : String,
    val isLogin : Boolean = false
)
