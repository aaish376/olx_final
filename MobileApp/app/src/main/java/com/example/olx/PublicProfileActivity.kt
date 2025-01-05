package com.example.olx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PublicProfileActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    lateinit var imageList: Array<Int>
    lateinit var titleList: Array<String>
    lateinit var priceList: Array<Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public_profile) // Set the activity layout here

//        // Initialize RecyclerView directly from activity's view
//        recyclerView = findViewById(R.id.homeRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this) // Use 'this' for the context in activities
//        recyclerView.setHasFixedSize(true)
//
//        // Initialize data arrays
//        imageList = arrayOf(
//            R.drawable.mobile,
//            R.drawable.mobile2,
//            R.drawable.ac,
//            R.drawable.car1,
//            R.drawable.bike1,
//            R.drawable.airbuds1,
//            R.drawable.dell1,
//            R.drawable.bicycle
//        )
//
//        titleList = arrayOf(
//            "Samsung S10",
//            "Oppo Reno 11F 8/128",
//            "Orient AC 1.5T",
//
//            "Civic Honda City",
//            "Black 125 Honda genuine",
//            "Audionic airbuds 495",
//
//            "Dell e5300 i5-8th gen 8/256ssd",
//            "Red Racer r30 gear bicycle"
//        )
//
//        priceList = arrayOf(
//            2000.0,
//            2000.0,
//            2000.0,
//
//            2000.0,
//            2000.0,
//            2000.0,
//
//            2000.0,
//            2000.0
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
//            val dataClass = DataClass(imageList[i], titleList[i], priceList[i])
//            dataList.add(dataClass)
//        }
//        // Set the adapter for the RecyclerView
//        recyclerView.adapter = AdapterClass(dataList)
    }
}
