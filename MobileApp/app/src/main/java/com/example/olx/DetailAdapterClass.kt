package com.example.olx

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DetailsAdapter(private val detailsList: List<DetailDataClass>) :
    RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keyText: TextView = itemView.findViewById(R.id.keyText)
        val valueText: TextView = itemView.findViewById(R.id.valueText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = detailsList[position]
        holder.keyText.text = detail.key
        holder.valueText.text = detail.value

        // Set alternating background colors
        val backgroundColor = if (position % 2 == 0) {
            Color.parseColor("#F2F2F2") // Light gray color
        } else {
            Color.WHITE
        }
        holder.itemView.setBackgroundColor(backgroundColor)
    }

    override fun getItemCount(): Int = detailsList.size
}
