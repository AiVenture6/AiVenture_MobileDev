package com.example.aiventure.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aiventure.R
import com.example.aiventure.category.CategoryItem
import com.google.android.material.chip.Chip

class CategoryAdapter(private val onItemClick: (CategoryItem, Int) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val data: ArrayList<CategoryItem> = arrayListOf()

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPlace: Chip = itemView.findViewById(R.id.category)

        fun bind(data: CategoryItem) {
            ivPlace.text = data.data
            ivPlace.isChecked = data.selected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onItemClick(data[position], position)
        }
    }

    override fun getItemCount(): Int = data.size

    fun submitList(newList: List<CategoryItem>) {
        this.data.clear()
        this.data.addAll(newList)
        notifyDataSetChanged()
    }
}