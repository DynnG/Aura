package com.example.clotheorganizer

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(private val clothes: List<Clothes>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val cloth = clothes[position]
        if (cloth.image_path != null) {
            try {
                 Glide.with(holder.itemView.context)
                    .load(Uri.parse(cloth.image_path))
                    .into(holder.imageView)
            } catch (e: Exception) {
                holder.imageView.setImageResource(R.drawable.cardview_background2) // Fallback
            }
        }
    }

    override fun getItemCount(): Int = clothes.size
}
