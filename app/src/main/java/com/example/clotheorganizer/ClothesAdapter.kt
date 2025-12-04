package com.example.clotheorganizer

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ClothesAdapter(
    private val context: Context,
    private val clothesList: List<Clothes>
) : RecyclerView.Adapter<ClothesAdapter.ClothesViewHolder>() {

    class ClothesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCloth: ImageView = itemView.findViewById(R.id.imgCloth)
        val txtClothName: TextView = itemView.findViewById(R.id.txtClothName)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)
        val toggleStatus: Switch = itemView.findViewById(R.id.toggleStatus)
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
                    .placeholder(R.drawable.upload) // Ensure this drawable exists or change it
                    .error(R.drawable.upload)
                    .into(holder.imgCloth)
            } catch (e: Exception) {
                holder.imgCloth.setImageResource(R.drawable.upload)
            }
        } else {
            holder.imgCloth.setImageResource(R.drawable.upload)
        }

        // Status logic (Example)
        // holder.txtStatus.text = if (cloth.statusID == 1) "Clean" else "Laundry"
        // holder.toggleStatus.isChecked = cloth.statusID == 1
    }

    override fun getItemCount(): Int {
        return clothesList.size
    }
}
