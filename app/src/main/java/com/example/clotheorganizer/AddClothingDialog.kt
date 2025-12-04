package com.example.clotheorganizer

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText

class AddClothingDialog(
    context: Context,
    private val pickImageLauncher: ActivityResultLauncher<Array<String>>,
    private val onAddListener: (Clothes) -> Unit
) : Dialog(context) {

    private lateinit var imgCloth: ShapeableImageView
    private lateinit var txtClothesName: TextInputEditText
    private lateinit var cgCategories: ChipGroup
    private lateinit var layoutTypes: LinearLayout
    private lateinit var cgTypesTops: ChipGroup
    private lateinit var cgTypesBottoms: ChipGroup
    private lateinit var cgTypesAccessories: ChipGroup
    private lateinit var cgStatus: ChipGroup
    private lateinit var btnDone: Button

    private var selectedImageUri: Uri? = null
    private var selectedCategory: String? = null
    private var selectedType: String? = null
    private var selectedStatus: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(context).inflate(R.layout.add_clothing, null)
        setContentView(view)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        imgCloth = view.findViewById(R.id.imgCloth)
        txtClothesName = view.findViewById(R.id.txtClothesName)
        cgCategories = view.findViewById(R.id.cgCategories)
        layoutTypes = view.findViewById(R.id.layoutTypes)
        cgTypesTops = view.findViewById(R.id.cgTypesTops)
        cgTypesBottoms = view.findViewById(R.id.cgTypesBottoms)
        cgTypesAccessories = view.findViewById(R.id.cgTypesAccessories)
        cgStatus = view.findViewById(R.id.cgStatus)
        btnDone = view.findViewById(R.id.btnDone)

        imgCloth.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        cgCategories.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds[0])
                selectedCategory = chip.text.toString()
                layoutTypes.visibility = View.VISIBLE
                updateTypesVisibility(selectedCategory)
            } else {
                selectedCategory = null
                layoutTypes.visibility = View.GONE
            }
        }

        setupTypeSelection(cgTypesTops)
        setupTypeSelection(cgTypesBottoms)
        setupTypeSelection(cgTypesAccessories)

        cgStatus.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds[0])
                selectedStatus = chip.text.toString()
            } else {
                selectedStatus = null
            }
        }

        btnDone.setOnClickListener {
            val name = txtClothesName.text.toString()
            if (name.isEmpty() || selectedImageUri == null || selectedCategory == null || selectedType == null || selectedStatus == null) {
                Toast.makeText(context, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            } else {
                val clothes = Clothes(
                    name = name,
                    image_path = selectedImageUri.toString(),
                    categoryID = getCategoryID(selectedCategory),
                    typeID = getTypeID(selectedType),
                    statusID = if (selectedStatus == "Available") 1 else 2, // 1=Clean, 2=Laundry
                    userID = 1, // Hardcoded
                    date_added = System.currentTimeMillis().toString(),
                    usage_count = 0
                )
                onAddListener(clothes)
                dismiss()
            }
        }
    }

    fun setImage(uri: Uri) {
        selectedImageUri = uri
        imgCloth.setImageURI(uri)
    }

    private fun updateTypesVisibility(category: String?) {
        cgTypesTops.visibility = View.GONE
        cgTypesBottoms.visibility = View.GONE
        cgTypesAccessories.visibility = View.GONE
        cgTypesTops.clearCheck()
        cgTypesBottoms.clearCheck()
        cgTypesAccessories.clearCheck()
        selectedType = null

        when (category) {
            "Tops" -> cgTypesTops.visibility = View.VISIBLE
            "Bottoms" -> cgTypesBottoms.visibility = View.VISIBLE
            "Accessories" -> cgTypesAccessories.visibility = View.VISIBLE
        }
    }

    private fun setupTypeSelection(chipGroup: ChipGroup) {
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds[0])
                selectedType = chip.text.toString()
            }
        }
    }

    // Helper methods to map strings to IDs (you might want to fetch these from DB ideally)
    private fun getCategoryID(category: String?): Int {
        return when (category) {
            "Tops" -> 1
            "Bottoms" -> 2
            "Accessories" -> 3
            else -> 1
        }
    }

    private fun getTypeID(type: String?): Int {
        if (type == null) return 1
        val dbHelper = AuraDBHelper(context)
        val typeId = dbHelper.getTypeId(type)
        dbHelper.close()
        return typeId ?: 1
    }
}
