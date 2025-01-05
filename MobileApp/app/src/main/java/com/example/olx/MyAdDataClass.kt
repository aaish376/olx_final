package com.example.olx

import java.util.Date

data class MyAdDataClass(
    val id:Int,
    val imageUrl: String, // The URL of the image
    val title: String,
    val price: String,
    val category:String,
    val des:String,
    val date: Date
    )
