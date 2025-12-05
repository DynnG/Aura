package com.example.clotheorganizer

import android.os.Bundle
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
        
        sliderTops.setImageList(topSlideList, ScaleTypes.FIT)
        sliderBottoms.setImageList(bottomSlideList, ScaleTypes.FIT)
        
        // Reset indices
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
        
        sliderCard1.setImageList(card1List, ScaleTypes.FIT)
        sliderCard2.setImageList(card2List, ScaleTypes.FIT)
        sliderCard3.setImageList(card3List, ScaleTypes.FIT)
    }
    
    private fun wearOutfit() {
        var updated = false
        
        // Update Top
        if (topsList.isNotEmpty() && currentTopIndex >= 0 && currentTopIndex < topsList.size) {
            val top = topsList[currentTopIndex]
            dbHelper.updateClothStatus(top.clothesID!!, 2) // 2 for Laundry
            updated = true
        }
        
        // Update Bottom
        if (bottomsList.isNotEmpty() && currentBottomIndex >= 0 && currentBottomIndex < bottomsList.size) {
            val bottom = bottomsList[currentBottomIndex]
            dbHelper.updateClothStatus(bottom.clothesID!!, 2) // 2 for Laundry
            updated = true
        }
        
        if (updated) {
            Toast.makeText(requireContext(), "Updated status to Laundry", Toast.LENGTH_SHORT).show()
            loadClothes() // Refresh sliders
        } else {
            Toast.makeText(requireContext(), "No outfit selected", Toast.LENGTH_SHORT).show()
        }
    }
}
