package com.example.clotheorganizer

import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent

class dashboard : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // --------- SearchView Setup ----------
        var isSearchOpen = false
        val lottieView = findViewById<LottieAnimationView>(R.id.lottieView)
        val searchView = findViewById<SearchView>(R.id.searchView)
        val searchEditText = searchView.findViewById<android.widget.EditText>(
            androidx.appcompat.R.id.search_src_text
        )
        searchEditText?.let {
            it.setTextColor(getColor(R.color.search_text))
            it.setHintTextColor(getColor(R.color.search_hint))
        }
        val searchIconId = resources.getIdentifier("android:id/search_mag_icon", null, null)
        val searchIcon = searchView.findViewById<ImageView>(searchIconId)
        searchIcon?.setImageDrawable(null)
        searchIcon?.layoutParams = ViewGroup.LayoutParams(0, 0)
        lottieView.progress = 0f

        lottieView.setOnClickListener {
            if (!isSearchOpen) {
                lottieView.setMinAndMaxProgress(0f, 0.5f)
                lottieView.playAnimation()
                searchView.visibility = View.VISIBLE
                searchView.isIconified = false
                searchView.requestFocus()
                isSearchOpen = true
            } else {
                lottieView.setMinAndMaxProgress(0.5f, 1f)
                lottieView.playAnimation()
                searchView.visibility = View.GONE
                isSearchOpen = false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --------- Spinner Setup ----------
        val topSpinner = findViewById<Spinner>(R.id.topSpinner)
        val bottomSpinner = findViewById<Spinner>(R.id.bottomSpinner)
        val accSpinner = findViewById<Spinner>(R.id.accSpinner)

        // Top options
        val topOptions = arrayOf("Tops", "Tshirt", "Polo", "Sleeveless")
        val topAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, topOptions) {
            override fun isEnabled(position: Int) = position != 0
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                view.setTextColor(if (position != 0) Color.WHITE else Color.WHITE)
                return view
            }
        }
        topAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        topSpinner.adapter = topAdapter

        // Bottom options
        val bottomOptions = arrayOf("Bottoms", "Jeans", "Trousers", "Shorts")
        val bottomAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bottomOptions) {
            override fun isEnabled(position: Int) = position != 0
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                return view
            }
        }
        bottomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bottomSpinner.adapter = bottomAdapter

        // Accessories options
        val accOptions = arrayOf("Accessories", "Hats", "Belts", "Watches", "Bags", "Sunglasses")
        val accAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accOptions) {
            override fun isEnabled(position: Int) = position != 0
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                view.setTextColor(if (position != 0) Color.WHITE else Color.WHITE)
                return view
            }
        }
        accAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        accSpinner.adapter = accAdapter

        // --- Bottom Navigation ---
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, dashboard::class.java)
                    startActivity(intent)
                    true
                }
                R.id.loglout -> {
                    Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginPage::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // --- FAB (Add) click: Image Picker ---
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                Toast.makeText(this, "Image Selected: $uri", Toast.LENGTH_SHORT).show()
                // TODO: Handle image (display or send to another activity)
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        fab.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }
}
