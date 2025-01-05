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
import java.util.Date

class MyAdFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: List<MyAdDataClass>
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_ad, container, false)

        dbHelper = DBHelper(requireContext())
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.myAdRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Use context from fragment
        recyclerView.setHasFixedSize(true)

        // Initialize data list
        dataList = arrayListOf()

        // Get data and set adapter
        fetchAds()

        return view
    }

    private fun fetchAds() {
        lifecycleScope.launch {
            // Call the API to fetch all ads
            try {
                val uId= dbHelper.getUserId()
                val response: Response<List<Ad>> = RetrofitInstance.api.getAllAds(uId)

                if (response.isSuccessful && response.body() != null) {
                    val ads = response.body()!!

                    // Map the received ads to DataClass
                    val dataList = ads.map { ad ->

                        MyAdDataClass(
                            ad.id,
                            ad.imageUrl, // imageUrl
                            ad.title,     // title
                            ad.price ,     // price
                            ad.category,
                            ad.description,
                            date = Date()
                        )
                    }

                    // Set the adapter
                    recyclerView.adapter = MyAdAdapterClass(dataList, this@MyAdFragment)
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch ads", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
