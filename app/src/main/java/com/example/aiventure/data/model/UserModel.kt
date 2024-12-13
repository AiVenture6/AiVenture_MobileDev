package com.example.aiventure.data.model

import java.util.Date

data class UserModel(
    val name: String,
    val email: String,
    val password: String,
    val otp: String?,
    val id: Int,
    val googleId: String?,
    val picture: String?,
    val otpExpiry: Date?,
    val isOtpVerified: Boolean,
    val createdAt: Date,
    val updatedAt: Date,
)