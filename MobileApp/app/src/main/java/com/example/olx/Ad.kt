package com.example.olx
data class Ad(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val condition: String,
    val category: String,
    val showPhoneNumber: Boolean,
    val imageUrl: String, // Image URL to load the image
    val userId:Int
)
//data class Ad(
//    val category: String,
//    val condition: String,
//    val title: String,
//    val description: String,
//    val price: String,
//    val showPhoneNumber: Boolean
//)

