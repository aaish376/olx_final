package com.example.olx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass> // Your DataClass for RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Use context from fragment
        recyclerView.setHasFixedSize(true)

        // Initialize data list
        dataList = arrayListOf()

        // Fetch data from server
        fetchAds()

        return view
    }

    private fun fetchAds() {
        lifecycleScope.launch {
            // Call the API to fetch all ads
            try {

                val response: Response<List<Ad>> = RetrofitInstance.api.getAllAds()

                if (response.isSuccessful && response.body() != null) {
                    val ads = response.body()!!

                    // Map the received ads to DataClass
                    val dataList = ads.map { ad ->

                        DataClass(
                            ad.imageUrl, // imageUrl
                            ad.title,     // title
                            ad.price ,     // price
                            ad.description
                        )
                    }

                    // Set the adapter
                    recyclerView.adapter = AdapterClass(dataList)
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch ads", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
