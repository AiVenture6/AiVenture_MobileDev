package com.example.aiventure.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Locale

class BookingTimeConverter {

    @TypeConverter
    fun fromBookingTime(bookingTime: BookingTime?): String? {
        return bookingTime?.let {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(it.time)
        }
    }

    @TypeConverter
    fun toBookingTime(timeString: String?): BookingTime? {
        return timeString?.let {
            val date = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(it)
            date?.let { BookingTime(it) }
        }
    }
}
