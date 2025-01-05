package com.example.olx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductDetailActivity : AppCompatActivity() {

    private var isDescriptionExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)


        // Retrieve data from Intent
        val imageUrl = intent.getStringExtra("imageUrl")
        val title = intent.getStringExtra("title")
        val price = intent.getStringExtra("price")
        val des = intent.getStringExtra("des")

        // Find views
        val imageView = findViewById<ImageView>(R.id.pdImage)
        val titleTextView = findViewById<TextView>(R.id.pdTitle)
        val priceTextView = findViewById<TextView>(R.id.pdPrice)

        // Set data to views
        Glide.with(this)
            .load("http://10.0.2.2:5288/${imageUrl}")
            .into(imageView)

        titleTextView.text = title
        priceTextView.text = "Rs $price"


//    val detailsList = listOf(
//        DetailDataClass("Make", "Toyota"),
//        DetailDataClass("Color", "Black"),
//        DetailDataClass("Model", "Corolla Altis"),
//        DetailDataClass("Number of seats", "4"),
//        DetailDataClass("Number of Owners", "2"),
//        DetailDataClass("Version", "1.6 Automatic"),
//        DetailDataClass("Assembly", "Local")
//    )

//    // Set up RecyclerView
//    val detailsRecyclerView: RecyclerView = findViewById(R.id.detailsRecyclerView)
//    detailsRecyclerView.layoutManager = LinearLayoutManager(this)
//    detailsRecyclerView.adapter = DetailsAdapter(detailsList)

        // Set up description and "See more" functionality
        val descriptionText: TextView = findViewById(R.id.descriptionText)
        val seeMoreButton: TextView = findViewById(R.id.seeMoreButton)

        // Example long description
        descriptionText.text = des

        // Toggle description text on button click
        seeMoreButton.setOnClickListener {
            if (isDescriptionExpanded) {
                // Collapse description to 6 lines
                descriptionText.maxLines = 6
                seeMoreButton.text = "See more"
            } else {
                // Expand description to full text
                descriptionText.maxLines = Int.MAX_VALUE
                seeMoreButton.text = "See less"
            }
            isDescriptionExpanded = !isDescriptionExpanded
        }

}
}


