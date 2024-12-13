package com.example.aiventure.recommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("price_range")
    val priceRange: Int,
    @SerializedName("ratings")
    val ratings: List<Float>,
    @SerializedName("distance")
    val distance: Float = 0f
) : Parcelable