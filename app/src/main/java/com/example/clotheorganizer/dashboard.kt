package com.example.clotheorganizer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        // Load HomeFragment by default
        supportFragmentManager.beginTransaction()
            .replace(R.id.item_container, HomeFragment())
            .commit()

        // Bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.item_container, HomeFragment())
                        .commit()
                    true
                }
                R.id.history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.item_container, HistoryFragment())
                        .commit()
                    true
                }
                R.id.outfit -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.item_container, OutfitsFragment())
                        .commit()
                    true
                }
                R.id.loglout -> {
                    finishAffinity()
                    true
                }
                else -> false
            }
        }

        // FAB: Image Picker
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                Toast.makeText(this, "Image Selected: $uri", Toast.LENGTH_SHORT).show()
                // TODO: Handle image
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        fab.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }
}
