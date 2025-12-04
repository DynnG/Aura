package com.example.clotheorganizer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var rvClothes: RecyclerView
    private lateinit var dbHelper: AuraDBHelper
    private lateinit var clothesList: MutableList<Clothes>
    private lateinit var adapter: ClothesAdapter

    // Cards
    private lateinit var cardAll: CardView
    private lateinit var cardClean: CardView
    private lateinit var cardLaundry: CardView
    private lateinit var textAll: TextView
    private lateinit var textClean: TextView
    private lateinit var textLaundry: TextView
    private lateinit var allBtn: AppCompatButton
    
    // Spinners
    private lateinit var topSpinner: Spinner
    private lateinit var bottomSpinner: Spinner
    private lateinit var accSpinner: Spinner
    
    private var activeFilter = "all" // default filter
    private var selectedType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = AuraDBHelper(requireContext())
        rvClothes = view.findViewById(R.id.rvClothes)
        
        // Cards
        cardAll = view.findViewById(R.id.all)
        cardClean = view.findViewById(R.id.clean)
        cardLaundry = view.findViewById(R.id.laundry)
        
        textAll = view.findViewById(R.id.txtAll)
        textClean = view.findViewById(R.id.txtClean)
        textLaundry = view.findViewById(R.id.txtLaundry)
        allBtn = view.findViewById(R.id.all_btn)
        
        topSpinner = view.findViewById(R.id.topSpinner)
        bottomSpinner = view.findViewById(R.id.bottomSpinner)
        accSpinner = view.findViewById(R.id.accSpinner)

        // Setup RecyclerView
        rvClothes.layoutManager = GridLayoutManager(requireContext(), 2)
        clothesList = ArrayList()
        adapter = ClothesAdapter(requireContext(), clothesList, dbHelper) { 
            refreshData()
        }
        rvClothes.adapter = adapter

        // Initial load
        applyFilters()
        updateCardUI(cardAll, textAll)

        // Click Listeners
        cardAll.setOnClickListener {
            filterClothes("all")
            updateCardUI(cardAll, textAll)
        }

        cardClean.setOnClickListener {
            filterClothes("clean")
            updateCardUI(cardClean, textClean)
        }

        cardLaundry.setOnClickListener {
            filterClothes("laundry")
            updateCardUI(cardLaundry, textLaundry)
        }
        
        allBtn.setOnClickListener { 
            selectedType = null
            resetSpinners()
            filterClothes("all")
            allBtn.setBackgroundResource(R.drawable.rounded_button_pressed)
            allBtn.setTextColor(Color.BLACK)
        }

        setupSpinner(
            topSpinner,
            arrayOf("Tops", "T-shirt", "Polo", "Sleeveless")
        )

        setupSpinner(
            bottomSpinner,
            arrayOf("Bottoms", "Trousers", "Jeans", "Sweatpants")
        )

        setupSpinner(
            accSpinner,
            arrayOf("Accessories", "Necklace", "Bracelets", "Bags", "Extra")
        )
    }

    fun refreshData() {
        applyFilters()
    }
    
    private fun filterClothes(filter: String) {
        activeFilter = filter
        applyFilters()
    }
    
    private fun filterClothesByType(typeName: String) {
        selectedType = typeName
        applyFilters()
        allBtn.setBackgroundResource(R.drawable.cardview2)
        allBtn.setTextColor(Color.WHITE)
    }

    private fun applyFilters() {
        if (!::dbHelper.isInitialized) return
        
        val baseList = if (selectedType != null) {
            dbHelper.getClothesByType(selectedType!!)
        } else {
            dbHelper.getAllClothes()
        }
        
        clothesList.clear()
        
        when (activeFilter) {
            "all" -> clothesList.addAll(baseList)
            "clean" -> clothesList.addAll(baseList.filter { it.statusID == 1 }) // Assuming 1 is clean
            "laundry" -> clothesList.addAll(baseList.filter { it.statusID == 2 }) // Assuming 2 is laundry
        }
        adapter.notifyDataSetChanged()
    }

    private fun updateCardUI(selectedCard: CardView, selectedText: TextView) {
        // Reset all cards to default
        resetCard(cardAll, textAll)
        resetCard(cardClean, textClean)
        resetCard(cardLaundry, textLaundry)

        // Highlight selected card
        val backgroundView = selectedCard.getChildAt(0)
        backgroundView.setBackgroundColor(Color.LTGRAY)
        selectedText.setTextColor(Color.BLACK)
    }

    private fun resetCard(card: CardView, text: TextView) {
        val backgroundView = card.getChildAt(0)
        backgroundView.setBackgroundResource(R.drawable.cardview2)
        text.setTextColor(Color.WHITE)
    }
    
    private fun resetSpinners() {
        topSpinner.setSelection(0)
        bottomSpinner.setSelection(0)
        accSpinner.setSelection(0)
    }
    
    private fun resetOtherSpinners(currentSpinnerId: Int) {
        if (topSpinner.id != currentSpinnerId) topSpinner.setSelection(0)
        if (bottomSpinner.id != currentSpinnerId) bottomSpinner.setSelection(0)
        if (accSpinner.id != currentSpinnerId) accSpinner.setSelection(0)
    }

    private fun setupSpinner(spinner: Spinner, items: Array<String>) {
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(Color.GRAY)
                } else {
                    view.setTextColor(Color.WHITE)
                }
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    // Reset others
                    resetOtherSpinners(spinner.id)
                    filterClothesByType(parent.getItemAtPosition(position).toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
