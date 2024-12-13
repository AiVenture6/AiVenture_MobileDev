package com.example.aiventure.category.detail

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.aiventure.R
import com.example.aiventure.booking.BookingBottomSheetFragment
import com.example.aiventure.category.datamodel.PlaceItemResponse
import com.google.android.material.chip.Chip

class DetailPlaceActivity : AppCompatActivity() {

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

        val place = intent?.getParcelableExtra<PlaceItemResponse>("place")
        btnBooking.setOnClickListener {
            val bookingBottomSheet = BookingBottomSheetFragment()
            bookingBottomSheet.setImagePlace(getFirstImage(place?.image.orEmpty()   ))
            bookingBottomSheet.show(supportFragmentManager, bookingBottomSheet.tag)
        }

        if (place != null) {
            Glide.with(this)
                .load(getFirstImage(place.image))
                .into(ivPlace)
            tvCity.text = place.city
            tvPlace.text = place.name
            tvDescription.text = place.description
            chipCategory.text = place.category
        }
    }

    private fun getFirstImage(jsonArrayString: String): String {
        val cleanedString = jsonArrayString.trim('"')
        val urlArray = cleanedString.trim('[', ']').split(",")
        val firstUrl = urlArray[0].trim().trim('"')
        return firstUrl.trim('\'')
    }

}