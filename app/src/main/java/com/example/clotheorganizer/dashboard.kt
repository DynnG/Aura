package com.example.clotheorganizer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class dashboard : AppCompatActivity() {

    private lateinit var dbHelper: AuraDBHelper
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private lateinit var addClothingDialog: AddClothingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dbHelper = AuraDBHelper(this)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        // Handle back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel()
                    finishAffinity() // Exit the app
                } else {
                    backToast = Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT)
                    backToast.show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.item_container, HomeFragment())
                .commit()
        }

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

        // Image Picker
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                try {
                    // Persist permission
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    // Set the image in the dialog
                    if (::addClothingDialog.isInitialized && addClothingDialog.isShowing) {
                        addClothingDialog.setImage(uri)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fab.setOnClickListener {
            addClothingDialog = AddClothingDialog(this, pickImageLauncher) { newCloth ->
                val id = dbHelper.addCloth(newCloth)
                if (id > -1) {
                    Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show()
                    // Refresh current fragment if it's HomeFragment
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.item_container)
                    if (currentFragment is HomeFragment) {
                        currentFragment.refreshData()
                    }
                } else {
                    Toast.makeText(this, "Error adding item", Toast.LENGTH_SHORT).show()
                }
            }
            addClothingDialog.show()
        }
    }
}
