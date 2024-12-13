package com.example.aiventure.category.datamodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HotelItemResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val priceRange: String,
    @SerializedName("rating_average")
    val ratings: Float = 0f,
    @SerializedName("image")
    val imageUrl: String
) : Parcelable