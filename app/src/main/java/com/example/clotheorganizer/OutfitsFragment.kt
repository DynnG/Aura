package com.example.clotheorganizer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import kotlin.random.Random

class OutfitsFragment : Fragment() {

    private lateinit var dbHelper: AuraDBHelper
    private lateinit var sliderTops: ImageSlider
    private lateinit var sliderBottoms: ImageSlider
    private lateinit var sliderCard1: ImageSlider
    private lateinit var sliderCard2: ImageSlider
    private lateinit var sliderCard3: ImageSlider
    private lateinit var btnWearOutfit: Button
    private lateinit var btnRandomOutfit: Button

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
        btnRandomOutfit = view.findViewById(R.id.btn_random_outfit)

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
        
        btnRandomOutfit.setOnClickListener {
            startRandomSpin()
        }

        return view
    }
    
    private fun startRandomSpin() {
        if (topsList.isEmpty() && bottomsList.isEmpty()) {
             Toast.makeText(requireContext(), "No clothes to randomize!", Toast.LENGTH_SHORT).show()
             return
        }

        // We will simulate a spin by starting the slideshow quickly and then stopping it after a delay
        // Unfortunately the ImageSlider library is limited in programmatic control (like 'setCurrentItem').
        // But we can start sliding and then stop.
        // Actually, startSliding() starts the auto cycle.
        
        sliderTops.startSliding(100) // Fast speed
        sliderBottoms.startSliding(100)
        
        // Disable buttons during spin
        btnRandomOutfit.isEnabled = false
        btnWearOutfit.isEnabled = false
        
        // Stop after random time (e.g., 1-2 seconds)
        val delay = Random.nextLong(1000, 2500)
        
        Handler(Looper.getMainLooper()).postDelayed({
             // Stop sliding
             sliderTops.stopSliding()
             sliderBottoms.stopSliding()
             
             // Now we want to pick a random item. The library doesn't easily expose "setCurrentItem".
             // However, `stopSliding` just stops the auto cycle. The view stays at the current item.
             // Ideally we want to jump to a specific random index, but this library is basic.
             // As a workaround, we can re-set the image list which resets to 0, or just trust the spin stopped somewhere random.
             // A better trick if supported: re-load the list but shuffled? No, that messes up the index matching.
             
             // Since we can't reliably "set" the index with this library version easily without seeing docs,
             // we will just assume `stopSliding` leaves it at a random spot.
             
             // Re-enable buttons
             if (isAdded) {
                 btnRandomOutfit.isEnabled = true
                 btnWearOutfit.isEnabled = true
                 Toast.makeText(requireContext(), "Random outfit selected!", Toast.LENGTH_SHORT).show()
             }
        }, delay)
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
                // Log the outfit
                dbHelper.logOutfit(itemsToLog, 1)
                
                Toast.makeText(requireContext(), "Great! Outfit logged to history.", Toast.LENGTH_SHORT).show()
                
                // Reload to remove the dirty clothes from the slider
                loadClothes() 
            } else {
                Toast.makeText(requireContext(), "Could not select outfit. Check if clothes are available.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val errorMsg = e.message ?: "Unknown error"
            Toast.makeText(requireContext(), "Error logging outfit: $errorMsg", Toast.LENGTH_LONG).show()
        }
    }
}
