package com.example.olx

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterClass(private val dataList: List<DataClass>) :
    RecyclerView.Adapter<AdapterClass.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ad = dataList[position]

        // Set the data (image, title, price)
        holder.title.text = ad.title
        holder.price.text = "RS ${ad.price}"

        // Load image (Use any image loading library like Glide or Picasso)
        Glide.with(holder.imageView.context)
            .load("http://10.0.2.2:5288/${ad.imageUrl}")
            .into(holder.imageView)

        // Set click listener for the entire item
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("imageUrl", ad.imageUrl)
            intent.putExtra("title", ad.title)
            intent.putExtra("price", ad.price)
            intent.putExtra("des", ad.des )
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.homeRecyclerViewImage)
        val title: TextView = itemView.findViewById(R.id.homeRecyclerViewTitle)
        val price: TextView = itemView.findViewById(R.id.homeRecyclerViewPrice)
    }
}

