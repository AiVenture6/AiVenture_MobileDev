package com.example.aiventure.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Locale

class BookingDateConverter {

    @TypeConverter
    fun fromBookingDate(bookingDate: BookingDate?): String? {
        return bookingDate?.let {
            SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it.date)
        }
    }

    @TypeConverter
    fun toBookingDate(dateString: String?): BookingDate? {
        return dateString?.let {
            val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).parse(it)
            date?.let { BookingDate(it) }
        }
    }
}
