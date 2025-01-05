package com.example.olx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass2>
    lateinit var imageList: Array<Int>
    lateinit var titleList: Array<String>
    lateinit var priceList: Array<Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites) // Set the activity layout here

//        // Initialize RecyclerView directly from activity's view
//        recyclerView = findViewById(R.id.homeRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this) // Use 'this' for the context in activities
//        recyclerView.setHasFixedSize(true)
//
//        // Initialize data arrays
//        imageList = arrayOf(
//            R.drawable.mobile,
//
//            R.drawable.airbuds1,
//
//        )
//
//        titleList = arrayOf(
//            "Samsung S10",
//
//            "Audionic airbuds 495",
//
//        )
//
//        priceList = arrayOf(
//            245000.0,
//            3500.0,
//
//
//        )
//
//        // Initialize data list
//        dataList = arrayListOf()
//
//        // Populate the data list and set the adapter
//        GetData()
//    }
//
//    private fun GetData() {
//        for (i in imageList.indices) {
//            val dataClass = DataClass2(imageList[i], titleList[i], priceList[i])
//            dataList.add(dataClass)
//        }
//        // Set the adapter for the RecyclerView
//        recyclerView.adapter = AdapterClass(dataList)
    }
}
