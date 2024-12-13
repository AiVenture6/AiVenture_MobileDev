package com.example.aiventure.category.detail

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.aiventure.R
import com.example.aiventure.category.datamodel.HotelItemResponse
import com.google.android.material.chip.Chip

class DetailHotelActivity : AppCompatActivity() {

    private lateinit var ivPlace: ImageView
    private lateinit var tvCity: TextView
    private lateinit var tvPlace: TextView
    private lateinit var tvDescription: TextView
    private lateinit var chipCategory: Chip
    private lateinit var btnBooking: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        ivPlace = findViewById(R.id.ivPlace)
        tvCity = findViewById(R.id.tvCity)
        tvPlace = findViewById(R.id.tvPlace)
        tvDescription = findViewById(R.id.tvDescription)
        chipCategory = findViewById(R.id.category)
        btnBooking = findViewById(R.id.btnBooking)
        btnBooking.isVisible = false

        val place = intent?.getParcelableExtra<HotelItemResponse>("place")

        if (place != null) {
            Glide.with(this)
                .load(getFirstImage(place.imageUrl))
                .into(ivPlace)
            tvCity.text = place.city
            tvPlace.text = place.name
            tvDescription.text = place.description
            chipCategory.isVisible = false
        }
    }

    private fun getFirstImage(jsonArrayString: String): String {
        val cleanedString = jsonArrayString.trim('"')
        val urlArray = cleanedString.trim('[', ']').split(",")
        val firstUrl = urlArray[0].trim().trim('"')
        return firstUrl.trim('\'')
    }

}