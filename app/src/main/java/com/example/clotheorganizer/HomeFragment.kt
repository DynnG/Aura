package com.example.clotheorganizer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
