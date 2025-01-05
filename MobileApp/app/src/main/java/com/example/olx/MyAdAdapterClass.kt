package com.example.olx

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class MyAdAdapterClass(
    private val dataList: List<MyAdDataClass>,

    private val lifecycleOwner: LifecycleOwner // Pass the lifecycle owner
) : RecyclerView.Adapter<MyAdAdapterClass.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_ad_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load("http://10.0.2.2:5288/${currentItem.imageUrl}")
            .into(holder.rvImg)

        holder.rvTitle.text = currentItem.title
        holder.rvPrice.text = "RS ${currentItem.price}"
        holder.rvCategory.text = currentItem.category
        holder.rvDate.text = "Posted on: ${currentItem.date}"

        // Delete button logic
        holder.deleteButton.setOnClickListener {
            lifecycleOwner.lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.api.deleteAd(currentItem.id)
                    if (response.isSuccessful) {
                        Toast.makeText(holder.itemView.context, "Ad deleted successfully", Toast.LENGTH_SHORT).show()
//                        dataList.removeAt(position)
//                        notifyItemRemoved(position)
                    } else {
                        Toast.makeText(holder.itemView.context, "Failed to delete ad", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(holder.itemView.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Edit button logic
        holder.editButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, SellActivity::class.java)
            intent.putExtra("adId", currentItem.id)
            intent.putExtra("title", currentItem.title)
            intent.putExtra("des", currentItem.des)
            intent.putExtra("price", currentItem.price)
            intent.putExtra("category", currentItem.category)
            intent.putExtra("condition", "Used")
            intent.putExtra("isEditMode", true)

            // Pass the Ad object to the SellActivity
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvImg: ImageView = itemView.findViewById(R.id.myAdRecyclerViewImage)
        val rvTitle: TextView = itemView.findViewById(R.id.myAdRecyclerViewTitle)
        val rvPrice: TextView = itemView.findViewById(R.id.myAdRecyclerViewPrice)
        val rvCategory: TextView = itemView.findViewById(R.id.myAdRecyclerViewCategory)
        val rvDate: TextView = itemView.findViewById(R.id.myAdRecyclerViewDate)
        val deleteButton: TextView = itemView.findViewById(R.id.btnDeleteAd)
        val editButton: TextView = itemView.findViewById(R.id.btnEditAd)
    }
}


//class MyAdAdapterClass(private val dataList: List<MyAdDataClass>) :
//    RecyclerView.Adapter<MyAdAdapterClass.ViewHolderClass>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.my_ad_item_layout, parent, false)
//        return ViewHolderClass(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
//        val currentItem = dataList[position]
//
//        // Use Glide to load the image from the URL
//        Glide.with(holder.itemView.context)
//            .load("http://10.0.2.2:5288/${currentItem.imageUrl}") // Image URL
//
//            .into(holder.rvImg)
//
//        // Bind other data to views
//        holder.rvTitle.text = currentItem.title
//        holder.rvPrice.text = "RS ${currentItem.price}" // Formatting price
//        holder.rvCategory.text = "${currentItem.category}"
//        holder.rvDate.text = "Posted on: ${currentItem.date}"
//
//
//        // Set click listener for the entire item
//        holder.itemView.setOnClickListener {
//            val context = holder.itemView.context
//            val intent = Intent(context, ProductDetailActivity::class.java)
//            intent.putExtra("imageUrl", currentItem.imageUrl)
//            intent.putExtra("title", currentItem.title)
//            intent.putExtra("price", currentItem.price)
//            intent.putExtra("des", currentItem.des )
//            context.startActivity(intent)
//        }
//
//    }
//
//    override fun getItemCount(): Int {
//        return dataList.size
//    }
//
//    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val rvImg: ImageView = itemView.findViewById(R.id.myAdRecyclerViewImage)
//        val rvTitle: TextView = itemView.findViewById(R.id.myAdRecyclerViewTitle)
//        val rvPrice: TextView = itemView.findViewById(R.id.myAdRecyclerViewPrice)
//        val rvCategory: TextView = itemView.findViewById(R.id.myAdRecyclerViewCategory)
//        val rvDate: TextView = itemView.findViewById(R.id.myAdRecyclerViewDate)
//
//    }
//}
