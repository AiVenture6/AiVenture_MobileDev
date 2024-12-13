package com.example.aiventure.category.datamodel

import android.os.Parcelable
import android.util.Log
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

@Parcelize
data class PlaceItemResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("image_url")
    val image: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("price_range")
    val priceRange: Int,
    @SerializedName("rating_average")
    val ratings: Float = 0f,
    @SerializedName("distance")
    val distance: Float = 0f
) : Parcelable