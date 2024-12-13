package com.example.aiventure.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aiventure.R
import com.example.aiventure.database.Ticket
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.Locale

class TicketAdapter(private val onItemClick: (Ticket) -> Unit) :
    RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    private val ticketList: MutableList<Ticket> = mutableListOf()

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPlace: ImageView = itemView.findViewById(R.id.ivPlace)
        val tvPlace: TextView = itemView.findViewById(R.id.tvPlace)
        val status: Chip = itemView.findViewById(R.id.status)
        val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketList[position]

        holder.tvPlace.text = ticket.name

        holder.status.text = "Approved"

//        holder.tvDetails.text = "Date: ${
//            SimpleDateFormat(
//                "dd MMMM yyyy",
//                Locale.getDefault()
//            ).format(ticket.date.date)
//        }"

        holder.itemView.setOnClickListener {
            onItemClick(ticket)
        }

        Glide.with(holder.itemView.context)
            .load(ticket.imageUrl)
            .into(holder.ivPlace)
    }

    fun submitList(newList: MutableList<Ticket>) {
        this.ticketList.clear()
        this.ticketList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = ticketList.size
}
