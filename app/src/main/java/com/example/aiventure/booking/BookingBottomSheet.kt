package com.example.aiventure.booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.example.aiventure.R
import com.example.aiventure.database.AppDatabase
import com.example.aiventure.database.BookingDate
import com.example.aiventure.database.BookingTime
import com.example.aiventure.database.Ticket
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BookingBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var nameEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var bookingButton: TextView

    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    private var selectedDate: Date? = null
    private var selectedTime: Date? = null
    private var imagePlace: String = ""

    fun setImagePlace(imageUrl: String) {
        imagePlace = imageUrl
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_layout, container, false)

        nameEditText = view.findViewById(R.id.nameEditText)
        quantityEditText = view.findViewById(R.id.quantityEditText)
        dateEditText = view.findViewById(R.id.dateEditText)
        timeEditText = view.findViewById(R.id.timeEditText)
        bookingButton = view.findViewById(R.id.bookingButton)

        dateEditText.setOnClickListener { openDatePicker() }
        timeEditText.setOnClickListener { openTimePicker() }

        bookingButton.setOnClickListener {
            saveBookingData()
        }

        return view
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                selectedDate = calendar.time
                dateEditText.setText(dateFormat.format(selectedDate!!))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun openTimePicker() {
        val calendar = Calendar.getInstance()

        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                selectedTime = calendar.time
                timeEditText.setText(timeFormat.format(selectedTime!!))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun saveBookingData() {
        val name = nameEditText.text.toString()
        val quantity = quantityEditText.text.toString().toIntOrNull()

        if (name.isEmpty() || quantity == null || selectedDate == null || selectedTime == null) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val ticket = Ticket(
            name = name,
            quantity = quantity,
            date = BookingDate(selectedDate!!),
            time = BookingTime(selectedTime!!),
            imageUrl = imagePlace
        )

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())

            db.ticketDao().insert(ticket)
            activity?.runOnUiThread {
                Toast.makeText(context, "Booking successful!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}
