package com.example.aiventure.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TicketDao {

    @Insert
    suspend fun insert(ticket: Ticket)

    @Query("SELECT * FROM ticket_table")
    suspend fun getAllTickets(): List<Ticket>
}
