package com.example.aiventure.notifications

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aiventure.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NotificationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_detail)

        val ivBack: ImageView = findViewById(R.id.ivBack)
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val tvDateTime: TextView = findViewById(R.id.tvDateTime)

        val notification = intent.getParcelableExtra<Notification>("notification")

        if (notification != null) {
            // Set the notification details
            tvTitle.text = notification.title
            tvDescription.text = notification.description
            tvDateTime.text = formatDate(notification.updatedAt)
        }

        ivBack.setOnClickListener {
            finish()
        }
    }

    private fun formatDate(dateTime: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm 'WIB'", Locale.getDefault())
            val date = inputFormat.parse(dateTime) ?: Date()
            outputFormat.format(date)
        } catch (e: Exception) {
            dateTime
        }
    }
}