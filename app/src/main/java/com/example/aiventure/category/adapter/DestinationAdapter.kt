package com.example.aiventure.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aiventure.R
import com.example.aiventure.category.datamodel.PlaceItemResponse

class DestinationAdapter(private val onItemClick: (PlaceItemResponse) -> Unit) :
    RecyclerView.Adapter<DestinationAdapter.PlaceViewHolder>() {

    private val places: ArrayList<PlaceItemResponse> = arrayListOf()

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPlace: ImageView = itemView.findViewById(R.id.ivPlace)
        private val tvPlace: TextView = itemView.findViewById(R.id.tvPlace)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)

        fun bind(place: PlaceItemResponse) {
            Glide.with(itemView.context)
                .load(getFirstImage(place.image))
                .into(ivPlace)
            tvPlace.text = place.name
            tvPrice.text = "Rp. ${place.priceRange}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    private fun getFirstImage(jsonArrayString: String): String {
        val cleanedString = jsonArrayString.trim('"')
        val urlArray = cleanedString.trim('[', ']').split(",")
        val firstUrl = urlArray[0].trim().trim('"')
        return firstUrl.trim('\'')
    }


    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(places[position])
        holder.itemView.setOnClickListener {
            onItemClick(places[position])
        }
    }

    override fun getItemCount(): Int = places.size

    fun submitList(newList: List<PlaceItemResponse>) {
        this.places.clear()
        this.places.addAll(newList)
        notifyDataSetChanged()
    }
}