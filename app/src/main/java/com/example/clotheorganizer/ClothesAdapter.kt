package com.example.clotheorganizer

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ClothesAdapter(
    private val context: Context,
    private val clothesList: List<Clothes>,
    private val dbHelper: AuraDBHelper,
    private val onStatusUpdated: () -> Unit
) : RecyclerView.Adapter<ClothesAdapter.ClothesViewHolder>() {

    class ClothesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCloth: ImageView = itemView.findViewById(R.id.imgCloth)
        val txtClothName: TextView = itemView.findViewById(R.id.txtClothName)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)
        val toggleStatus: SwitchCompat = itemView.findViewById(R.id.toggleStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_clothing, parent, false)
        return ClothesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothesViewHolder, position: Int) {
        val cloth = clothesList[position]
        holder.txtClothName.text = cloth.name ?: "Unknown"

        // Load Image
        if (!cloth.image_path.isNullOrEmpty()) {
            try {
                Glide.with(context)
                    .load(Uri.parse(cloth.image_path))
                    .placeholder(R.drawable.upload)
                    .error(R.drawable.upload)
                    .into(holder.imgCloth)
            } catch (e: Exception) {
                holder.imgCloth.setImageResource(R.drawable.upload)
            }
        } else {
            holder.imgCloth.setImageResource(R.drawable.upload)
        }

        // Status logic
        val isClean = cloth.statusID == 1
        // Set listener to null before changing checked state to prevent infinite loop
        holder.toggleStatus.setOnCheckedChangeListener(null)
        holder.toggleStatus.isChecked = isClean
        updateStatusUI(holder, isClean)

        // Handle Toggle Click
        holder.toggleStatus.setOnCheckedChangeListener { _, isChecked ->
            updateStatusUI(holder, isChecked)
            val newStatusId = if (isChecked) 1 else 2
            cloth.clothesID?.let {
                dbHelper.updateClothStatus(it, newStatusId)
                onStatusUpdated()
            }
        }
    }

    private fun updateStatusUI(holder: ClothesViewHolder, isClean: Boolean) {
        if (isClean) {
            holder.txtStatus.text = "Clean"
            holder.txtStatus.setTextColor(Color.WHITE)
            holder.toggleStatus.thumbTintList = ColorStateList.valueOf(Color.parseColor("#8C7BD8")) // Purple
            holder.toggleStatus.trackTintList = ColorStateList.valueOf(Color.parseColor("#D7D0F0")) // Light Purple
        } else {
            holder.txtStatus.text = "Laundry"
            holder.txtStatus.setTextColor(Color.GRAY)
            holder.toggleStatus.thumbTintList = ColorStateList.valueOf(Color.GRAY)
            holder.toggleStatus.trackTintList = ColorStateList.valueOf(Color.LTGRAY)
        }
    }

    override fun getItemCount(): Int {
        return clothesList.size
    }
}
