package com.example.aiventure.camera

data class CameraResponse(
    val imageUrl: String,
    val message: String,
    val scan: ScanDetails
)
