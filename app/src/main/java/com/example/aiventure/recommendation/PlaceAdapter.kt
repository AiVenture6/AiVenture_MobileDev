package com.example.aiventure.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aiventure.R

class PlaceAdapter(private val onItemClick: (PlaceResponse) -> Unit) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    private val places: ArrayList<PlaceResponse> = arrayListOf()

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPlace: ImageView = itemView.findViewById(R.id.ivPlace)
        private val tvPlace: TextView = itemView.findViewById(R.id.tvPlace)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)

        fun bind(place: PlaceResponse) {
            Glide.with(itemView.context)
                .load(place.imageUrl)
                .into(ivPlace)
            tvPlace.text = place.name
            tvPrice.text = "Rp. ${place.priceRange}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(places[position])
        holder.itemView.setOnClickListener {
            onItemClick(places[position])
        }
    }

    override fun getItemCount(): Int = places.size

    fun submitList(newList: List<PlaceResponse>) {
        this.places.clear()
        this.places.addAll(newList)
        notifyDataSetChanged()
    }
}