package com.example.clotheorganizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import java.io.File

class OutfitsFragment : Fragment() {

    private lateinit var dbHelper: AuraDBHelper
    private lateinit var sliderTops: ImageSlider
    private lateinit var sliderBottoms: ImageSlider
    private lateinit var sliderCard1: ImageSlider
    private lateinit var sliderCard2: ImageSlider
    private lateinit var sliderCard3: ImageSlider

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

        loadClothes()

        return view
    }

    private fun loadClothes() {
        val allClothes = dbHelper.getClothesForUser(1) // Assuming user 1
        
        val topList = ArrayList<SlideModel>()
        val bottomList = ArrayList<SlideModel>()
        val accessoryList = ArrayList<SlideModel>()
        
        for (cloth in allClothes) {
            if (cloth.statusID == 1) { // 1 is for clean clothes
                val imagePath = cloth.image_path
                if (!imagePath.isNullOrEmpty()) {
                    val slideModel = SlideModel(imagePath, ScaleTypes.FIT)
                    when (cloth.categoryID) {
                        1 -> topList.add(slideModel)
                        2 -> bottomList.add(slideModel)
                        3 -> accessoryList.add(slideModel)
                    }
                }
            }
        }
        
        sliderTops.setImageList(topList, ScaleTypes.FIT)
        sliderBottoms.setImageList(bottomList, ScaleTypes.FIT)
        
        val card1List = ArrayList<SlideModel>()
        val card2List = ArrayList<SlideModel>()
        val card3List = ArrayList<SlideModel>()
        
        for ((index, accessory) in accessoryList.withIndex()) {
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
}
