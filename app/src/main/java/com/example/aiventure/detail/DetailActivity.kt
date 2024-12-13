package com.example.aiventure.detail

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aiventure.R
import com.example.aiventure.booking.BookingBottomSheetFragment
import com.example.aiventure.recommendation.PlaceResponse
import com.google.android.material.chip.Chip

class DetailActivity : AppCompatActivity() {

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

        val place = intent?.getParcelableExtra<PlaceResponse>("place")

        btnBooking.setOnClickListener {
            val bookingBottomSheet = BookingBottomSheetFragment()
            bookingBottomSheet.setImagePlace(place?.imageUrl.orEmpty())
            bookingBottomSheet.show(supportFragmentManager, bookingBottomSheet.tag)
        }

        if (place != null) {
            Glide.with(this)
                .load(place.imageUrl)
                .into(ivPlace)
            tvCity.text = place.city
            tvPlace.text = place.name
            tvDescription.text = place.description
            chipCategory.text = place.category
        }
    }
}