package com.example.clotheorganizer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.icu.number.Scale
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel


class MainActivity : AppCompatActivity() {

    private lateinit var getStartedBtn: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle system bar insets (optional, auto-generated)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getStartedBtn = findViewById(R.id.getStartedBtn)

        getStartedBtn.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        val imageSliderTop: ImageSlider = findViewById(R.id.imageslider)
        val slideModelTop = ArrayList<SlideModel>()
        slideModelTop.add(SlideModel(R.drawable.top1, ScaleTypes.CENTER_INSIDE))
        slideModelTop.add(SlideModel(R.drawable.top2, ScaleTypes.CENTER_INSIDE))
        slideModelTop.add(SlideModel(R.drawable.top3, ScaleTypes.CENTER_INSIDE))


        val imageSlider: ImageSlider = findViewById(R.id.bottomslider)
        val slideModel = ArrayList<SlideModel>()
        slideModel.add(SlideModel(R.drawable.bottom1, ScaleTypes.CENTER_INSIDE))
        slideModel.add(SlideModel(R.drawable.bottom2, ScaleTypes.CENTER_INSIDE))
        slideModel.add(SlideModel(R.drawable.bottom3, ScaleTypes.CENTER_INSIDE))

        imageSliderTop.setImageList(slideModelTop, ScaleTypes.CENTER_INSIDE)
        imageSlider.setImageList(slideModel, ScaleTypes.CENTER_INSIDE)

    }
}
