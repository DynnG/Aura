package com.example.clotheorganizer

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistoryAdapter(
    private val context: Context,
    private val historyList: List<Outfit>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val imageSlider: ImageSlider = itemView.findViewById(R.id.image_slider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val outfit = historyList[position]
        
        // Format Date
        val date = outfit.date ?: 0L
        if (DateUtils.isToday(date)) {
            holder.tvDate.text = "Today"
        } else {
            val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            holder.tvDate.text = sdf.format(date)
        }

        // Setup ImageSlider
        val imageList = ArrayList<SlideModel>()
        for (item in outfit.items) {
             // Check if image path is valid
             if (!item.image_path.isNullOrEmpty()) {
                 imageList.add(SlideModel(item.image_path, ScaleTypes.FIT))
             } else {
                 // Fallback or skip
                 // imageList.add(SlideModel(R.drawable.cardview_background2, ScaleTypes.FIT))
             }
        }
        
        if (imageList.isNotEmpty()) {
            holder.imageSlider.setImageList(imageList)
        } else {
            // Clear slider or show empty state if needed
            // holder.imageSlider.setImageList(emptyList()) // Library might crash if empty?
        }
    }

    override fun getItemCount(): Int = historyList.size
}
