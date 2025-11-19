package com.example.clotheorganizer

import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatButton
import com.airbnb.lottie.LottieAnimationView

class HomeFragment : Fragment() {

    private lateinit var allBtn: AppCompatButton
    private var isSearchOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate your fragment layout here
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------------- FIND VIEWS ---------------------
        val lottieView = view.findViewById<LottieAnimationView>(R.id.lottieView)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val allBtn = view.findViewById<AppCompatButton>(R.id.all_btn)

        // Spinners
        val topSpinner = view.findViewById<Spinner>(R.id.topSpinner)
        val bottomSpinner = view.findViewById<Spinner>(R.id.bottomSpinner)
        val accSpinner = view.findViewById<Spinner>(R.id.accSpinner)

        // ------------ SEARCH VIEW SETUP --------------
        val searchEditText = searchView.findViewById<android.widget.EditText>(
            androidx.appcompat.R.id.search_src_text
        )
        searchEditText?.let {
            it.setTextColor(requireContext().getColor(R.color.search_text))
            it.setHintTextColor(requireContext().getColor(R.color.search_hint))
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

        // ----------------- CATEGORY BUTTON -------------------
        allBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Load ALL items", Toast.LENGTH_SHORT).show()
        }

        // ----------------- SPINNERS --------------------------
        setupSpinner(
            topSpinner,
            arrayOf("Tops", "Tshirt", "Polo", "Sleeveless")
        )

        setupSpinner(
            bottomSpinner,
            arrayOf("Bottoms", "Jeans", "Trousers", "Shorts")
        )

        setupSpinner(
            accSpinner,
            arrayOf("Accessories", "Hats", "Belts", "Watches", "Bags", "Sunglasses")
        )
    }

    // Function to setup all spinners
    private fun setupSpinner(spinner: Spinner, items: Array<String>) {
        val adapter = object :
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items) {

            override fun isEnabled(position: Int) = position != 0

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
