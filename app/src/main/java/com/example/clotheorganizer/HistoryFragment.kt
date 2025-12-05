package com.example.clotheorganizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryFragment : Fragment() {

    private lateinit var rvHistory: RecyclerView
    private lateinit var tvEmptyHistory: TextView
    private lateinit var dbHelper: AuraDBHelper
    private lateinit var historyList: List<Outfit>
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistory = view.findViewById(R.id.rvHistory)
        tvEmptyHistory = view.findViewById(R.id.tvEmptyHistory)
        dbHelper = AuraDBHelper(requireContext())

        setupRecyclerView()
    }
    
    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun setupRecyclerView() {
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        historyList = ArrayList()
        // Initial empty adapter
        adapter = HistoryAdapter(requireContext(), historyList)
        rvHistory.adapter = adapter
        
        refreshData()
    }
    
    private fun refreshData() {
        if (::dbHelper.isInitialized) {
            historyList = dbHelper.getAllOutfits()
            adapter = HistoryAdapter(requireContext(), historyList)
            rvHistory.adapter = adapter
            
            if (historyList.isEmpty()) {
                rvHistory.visibility = View.GONE
                tvEmptyHistory.visibility = View.VISIBLE
            } else {
                rvHistory.visibility = View.VISIBLE
                tvEmptyHistory.visibility = View.GONE
            }
        }
    }
}
