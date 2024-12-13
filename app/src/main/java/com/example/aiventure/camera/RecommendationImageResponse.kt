package com.example.aiventure.camera

import com.google.gson.annotations.SerializedName

data class RecommendationImageResponse(
    @SerializedName("cluster")
    val cluster: Int = 0,
    @SerializedName("recommendations")
    val recommendations: List<String> = emptyList(),
    @SerializedName("status")
    val status: String = ""
)