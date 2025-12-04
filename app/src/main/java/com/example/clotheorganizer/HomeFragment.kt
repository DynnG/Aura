package com.example.clotheorganizer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var rvClothes: RecyclerView
    private lateinit var dbHelper: AuraDBHelper
    private lateinit var clothesList: MutableList<Clothes>
    private lateinit var adapter: ClothesAdapter

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
        
        // Setup RecyclerView
        rvClothes.layoutManager = GridLayoutManager(requireContext(), 2)
        clothesList = ArrayList()
        adapter = ClothesAdapter(requireContext(), clothesList)
        rvClothes.adapter = adapter

        loadClothes()

        val topSpinner = view.findViewById<Spinner>(R.id.topSpinner)
        val bottomSpinner = view.findViewById<Spinner>(R.id.bottomSpinner)
        val accSpinner = view.findViewById<Spinner>(R.id.accSpinner)

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
            arrayOf("Accessories", "Bag", "Necklace", "Shoes")
        )
    }

    fun refreshData() {
        loadClothes()
    }

    private fun loadClothes() {
        if (::dbHelper.isInitialized && ::clothesList.isInitialized) {
            clothesList.clear()
            clothesList.addAll(dbHelper.getAllClothes())
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupSpinner(spinner: Spinner, items: Array<String>) {
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item (header)
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    // Set the header color to gray
                    view.setTextColor(Color.GRAY)
                } else {
                    // Set the color for other items to white, as the dropdown is dark
                    view.setTextColor(Color.WHITE)
                }
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
