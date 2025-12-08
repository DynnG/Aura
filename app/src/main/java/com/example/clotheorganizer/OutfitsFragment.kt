package com.example.clotheorganizer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.models.SlideModel
import java.io.File

class OutfitsFragment : Fragment() {

    private lateinit var dbHelper: AuraDBHelper
    private lateinit var sliderTops: ImageSlider
    private lateinit var sliderBottoms: ImageSlider
    private lateinit var sliderCard1: ImageSlider
    private lateinit var sliderCard2: ImageSlider
    private lateinit var sliderCard3: ImageSlider
    private lateinit var btnWearOutfit: Button

    private var topsList: MutableList<Clothes> = ArrayList()
    private var bottomsList: MutableList<Clothes> = ArrayList()
    private var currentTopIndex = 0
    private var currentBottomIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_outfits, container, false)
        
        dbHelper = AuraDBHelper(requireContext())

        sliderTops = view.findViewById(R.id.slider_tops)
        sliderBottoms = view.findViewById(R.id.slider_bottoms)
        sliderCard1 = view.findViewById(R.id.slider_1)
        sliderCard2 = view.findViewById(R.id.slider_2)
        sliderCard3 = view.findViewById(R.id.slider_card3)
        btnWearOutfit = view.findViewById(R.id.btn_wear_outfit)

        loadClothes()
        
        sliderTops.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChanged(position: Int) {
                currentTopIndex = position
            }
        })
        
        sliderBottoms.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChanged(position: Int) {
                currentBottomIndex = position
            }
        })
        
        btnWearOutfit.setOnClickListener {
            wearOutfit()
        }

        return view
    }

    private fun loadClothes() {
        try {
            val allClothes = dbHelper.getClothesForUser(1) // Assuming user 1
            
            topsList.clear()
            bottomsList.clear()
            
            val topSlideList = ArrayList<SlideModel>()
            val bottomSlideList = ArrayList<SlideModel>()
            val accessorySlideList = ArrayList<SlideModel>()
            
            for (cloth in allClothes) {
                if (cloth.statusID == 1) { // 1 is for clean clothes
                    val imagePath = cloth.image_path
                    if (!imagePath.isNullOrEmpty()) {
                        val slideModel = SlideModel(imagePath, ScaleTypes.FIT)
                        when (cloth.categoryID) {
                            1 -> {
                                topsList.add(cloth)
                                topSlideList.add(slideModel)
                            }
                            2 -> {
                                bottomsList.add(cloth)
                                bottomSlideList.add(slideModel)
                            }
                            3 -> accessorySlideList.add(slideModel)
                        }
                    }
                }
            }
            
            // Handle Tops Slider
            if (topSlideList.isNotEmpty()) {
                sliderTops.setImageList(topSlideList, ScaleTypes.FIT)
            } else {
                // If empty, passing an empty list might cause issues depending on library version.
                // It is better to clear it or set a dummy if needed, but here we try an empty list.
                sliderTops.setImageList(ArrayList<SlideModel>(), ScaleTypes.FIT) 
            }

            // Handle Bottoms Slider
            if (bottomSlideList.isNotEmpty()) {
                sliderBottoms.setImageList(bottomSlideList, ScaleTypes.FIT)
            } else {
                 sliderBottoms.setImageList(ArrayList<SlideModel>(), ScaleTypes.FIT)
            }
            
            // Reset indices to 0 whenever data is reloaded
            currentTopIndex = 0
            currentBottomIndex = 0
            
            val card1List = ArrayList<SlideModel>()
            val card2List = ArrayList<SlideModel>()
            val card3List = ArrayList<SlideModel>()
            
            for ((index, accessory) in accessorySlideList.withIndex()) {
                if (index % 3 == 0) {
                    card1List.add(accessory)
                } else if (index % 3 == 1) {
                    card2List.add(accessory)
                } else {
                    card3List.add(accessory)
                }
            }
            
            if (card1List.isNotEmpty()) sliderCard1.setImageList(card1List, ScaleTypes.FIT)
            if (card2List.isNotEmpty()) sliderCard2.setImageList(card2List, ScaleTypes.FIT)
            if (card3List.isNotEmpty()) sliderCard3.setImageList(card3List, ScaleTypes.FIT)
            
        } catch (e: Exception) {
            e.printStackTrace()
             // Avoid crashing during load
        }
    }
    
    private fun wearOutfit() {
        try {
            val itemsToLog = ArrayList<Clothes>()
            
            // Select Top
            if (topsList.isNotEmpty()) {
                 if (currentTopIndex >= 0 && currentTopIndex < topsList.size) {
                    val top = topsList[currentTopIndex]
                    itemsToLog.add(top)
                 }
            }
            
            // Select Bottom
            if (bottomsList.isNotEmpty()) {
                 if (currentBottomIndex >= 0 && currentBottomIndex < bottomsList.size) {
                    val bottom = bottomsList[currentBottomIndex]
                    itemsToLog.add(bottom)
                 }
            }
            
            if (itemsToLog.isNotEmpty()) {
                // Log the outfit (This method in DB helper also updates status to 2)
                dbHelper.logOutfit(itemsToLog, 1)
                
                Toast.makeText(requireContext(), "Great! Outfit logged to history.", Toast.LENGTH_SHORT).show()
                
                // Reload to remove the dirty clothes from the slider
                loadClothes() 
            } else {
                Toast.makeText(requireContext(), "Could not select outfit. Check if clothes are available.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Here is the fix: use e.message safely
            val errorMsg = e.message ?: "Unknown error"
            Toast.makeText(requireContext(), "Error logging outfit: $errorMsg", Toast.LENGTH_LONG).show()
        }
    }
}
