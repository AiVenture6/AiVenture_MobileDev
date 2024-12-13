package com.example.aiventure.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aiventure.R
import java.util.Timer
import java.util.TimerTask

class ImageAdapter(private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val imageList: MutableList<List<String>> = mutableListOf()
    private lateinit var timer: Timer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflater.inflate(R.layout.item_home, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val images = imageList[position]
        val imageView = holder.ivPlace

        Glide.with(context).load(images[0])
            .apply(RequestOptions().transform(RoundedCornersTransformation(20f)))
            .into(imageView)

        timer = Timer()
        timer.schedule(object : TimerTask() {
            var currentIndex = 0
            override fun run() {
                if (currentIndex < images.size - 1) {
                    currentIndex++
                } else {
                    currentIndex = 0
                }
                (holder.itemView).post {
                    val fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f)
                    val fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f)
                    fadeOut.duration = 500
                    fadeIn.duration = 500

                    fadeOut.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            Glide.with(context).load(images[currentIndex]).into(imageView)
                            fadeIn.start()
                        }
                    })
                    fadeOut.start()
                }
            }
        }, 0, 3000)
    }

    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        timer.cancel()
    }

    override fun getItemCount(): Int = imageList.size

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivPlace: ImageView = view.findViewById(R.id.ivPlace)
    }

    fun submitList(images: MutableList<List<String>>) {
        this.imageList.clear()
        this.imageList.addAll(images)
        notifyDataSetChanged()
    }
}
