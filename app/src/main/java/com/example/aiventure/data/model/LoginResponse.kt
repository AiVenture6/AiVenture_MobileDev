package com.example.aiventure.data.model

data class LoginResponse(
    val user: UserModel,
    val token: String
)