package com.example.aiventure.booking

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aiventure.R
import com.example.aiventure.database.AppDatabase
import com.example.aiventure.database.BookingDate
import com.example.aiventure.database.BookingTime
import com.example.aiventure.database.Ticket
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class BookingFragment : Fragment() {

    private lateinit var ticketAdapter: TicketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvReservations)

        ticketAdapter = TicketAdapter {
            showTicketDialog(it)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ticketAdapter

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val tickets = db.ticketDao().getAllTickets()
            ticketAdapter.submitList(tickets.toMutableList())
        }
    }

    private fun showTicketDialog(ticket: Ticket) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_ticket)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val titleText: TextView = dialog.findViewById(R.id.title)
        val idText: TextView = dialog.findViewById(R.id.idText)
        val nameText: TextView = dialog.findViewById(R.id.nameText)
        val visitorsText: TextView = dialog.findViewById(R.id.visitorsText)
        val dateText: TextView = dialog.findViewById(R.id.dateText)
        val timeText: TextView = dialog.findViewById(R.id.timeText)

        titleText.text = "Ai-Venture"
        idText.text = "ID : ${ticket.id}"
        nameText.text = "Name : ${ticket.name}"
        visitorsText.text = "Visitors : ${ticket.quantity}"
        dateText.text = "Date : ${ticket.date.formattedDate}"
        timeText.text = "Time : ${ticket.time.formattedTime}"

        dialog.show()
    }
}

val BookingDate.formattedDate: String
    get() = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(this.date)

val BookingTime.formattedTime: String
    get() = SimpleDateFormat("HH:mm", Locale.getDefault()).format(this.time)