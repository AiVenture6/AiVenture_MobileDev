package com.example.aiventure.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ticket_table")
data class Ticket(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: Int,
    val date: BookingDate,
    val time: BookingTime,
    val imageUrl: String
)
