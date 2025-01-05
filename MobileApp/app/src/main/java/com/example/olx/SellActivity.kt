package com.example.olx

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SellActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var conditionSpinner: Spinner
    private lateinit var adTitle: EditText
    private lateinit var adDescription: EditText
    private lateinit var adPrice: EditText
    private lateinit var phoneNumberSwitch: Switch
    private lateinit var postButton: Button
    private lateinit var addImage: TextView
    private var selectedImageUri: Uri? = null

    private lateinit var dbHelper: DBHelper

    private var isEditMode = false
    private var adId: Int = 0

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val STORAGE_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)

        categorySpinner = findViewById(R.id.spinner_category)
        conditionSpinner = findViewById(R.id.spinner_condition)
        adTitle = findViewById(R.id.et_ad_title)
        adDescription = findViewById(R.id.et_ad_description)
        adPrice = findViewById(R.id.et_ad_price)
        phoneNumberSwitch = findViewById(R.id.switch_phone_number)
        postButton = findViewById(R.id.post_btn)
        addImage = findViewById(R.id.add_image)

        dbHelper = DBHelper(this)

        checkStoragePermission()
        setupSpinners()

        // Check if activity is launched for editing
        isEditMode = intent.getBooleanExtra("isEditMode", false)

        if (isEditMode) {
            prefillAdDetails()
            postButton.text = "Update Ad"
        }

        addImage.setOnClickListener {
            pickImageFromGallery()
        }

        postButton.setOnClickListener {
            if (isEditMode) {
                updateAd()
            } else {
                submitAd()
            }
        }
    }

    private fun setupSpinners() {
        val categories = arrayOf("Mobile", "Car", "Laptop", "Bikes")
        val conditions = arrayOf("Used", "New", "Refurbished")

        categorySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        conditionSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, conditions)
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitAd() {
        val category = categorySpinner.selectedItem.toString()
        val condition = conditionSpinner.selectedItem.toString()
        val title = adTitle.text.toString()
        val description = adDescription.text.toString()
        val price = adPrice.text.toString()
        val showPhoneNumber = phoneNumberSwitch.isChecked
        val uId = dbHelper.getUserId()

        val ad = Ad(
            id = 0,
            userId = uId,
            imageUrl = " ",
            category = category,
            condition = condition,
            title = title,
            description = description,
            price = price,
            showPhoneNumber = showPhoneNumber
        )
        Log.d("AdDetails", ad.toString())

        selectedImageUri?.let { uri ->
            val filePath = getFileFromUri(uri)
            filePath?.let { file ->
                sendToServer(ad, file)
            }
        } ?: run {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun prefillAdDetails() {
        adId = intent.getIntExtra("adId", 0)
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("des")
        val price = intent.getStringExtra("price")
        val category = intent.getStringExtra("category")
        val condition = intent.getStringExtra("condition")

        adTitle.setText(title)
        adDescription.setText(description)
        adPrice.setText(price)

        val categoryIndex = (categorySpinner.adapter as ArrayAdapter<String>).getPosition(category)
        categorySpinner.setSelection(categoryIndex)

        val conditionIndex = (conditionSpinner.adapter as ArrayAdapter<String>).getPosition(condition)
        conditionSpinner.setSelection(conditionIndex)
    }

    private fun updateAd() {
        val category = categorySpinner.selectedItem.toString()
        val condition = conditionSpinner.selectedItem.toString()
        val title = adTitle.text.toString()
        val description = adDescription.text.toString()
        val price = adPrice.text.toString()
        val showPhoneNumber = phoneNumberSwitch.isChecked

        val ad = Ad(
            id = adId,
            userId = dbHelper.getUserId(),
            imageUrl = " ",
            category = category,
            condition = condition,
            title = title,
            description = description,
            price = price,
            showPhoneNumber = showPhoneNumber
        )

        selectedImageUri?.let { uri ->
            val filePath = getFileFromUri(uri)
            filePath?.let { file ->
                sendToServer(ad, file, isUpdate = true)
            }
        } ?: run {
            sendToServer(ad, null, isUpdate = true)
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            Log.e("FileError", "Failed to get file from URI", e)
            null
        }
    }

    private fun sendToServer(ad: Ad, imageFile: File?, isUpdate: Boolean = false) {
        val adJson = Gson().toJson(ad)
        val adRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), adJson)

        val imagePart = imageFile?.let {
            val imageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), it)
            MultipartBody.Part.createFormData("image", it.name, imageRequestBody)
        }

        lifecycleScope.launch {
            val response = if (isUpdate) {
                RetrofitInstance.api.updateAd(ad.id, adRequestBody, imagePart)
            } else {
                RetrofitInstance.api.postAd(adRequestBody, imagePart!!)
            }

            if (response.isSuccessful) {
                val message = if (isUpdate) "Ad updated successfully!" else "Ad posted successfully!"
                Toast.makeText(this@SellActivity, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SellActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@SellActivity, "Failed to process ad", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

//package com.example.olx
//
//import android.Manifest
//import android.app.Activity
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContentProviderCompat.requireContext
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.lifecycleScope
//import com.google.gson.Gson
//import kotlinx.coroutines.launch
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import java.io.File
//
//class SellActivity : AppCompatActivity() {
//
//    private lateinit var categorySpinner: Spinner
//    private lateinit var conditionSpinner: Spinner
//    private lateinit var adTitle: EditText
//    private lateinit var adDescription: EditText
//    private lateinit var adPrice: EditText
//    private lateinit var phoneNumberSwitch: Switch
//    private lateinit var postButton: Button
//    private lateinit var addImage: TextView
//    private var selectedImageUri: Uri? = null
//
//    private lateinit var dbHelper: DBHelper
//
//    companion object {
//        private const val IMAGE_PICK_CODE = 1000
//        private const val STORAGE_PERMISSION_CODE = 101
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sell)
//
//        categorySpinner = findViewById(R.id.spinner_category)
//        conditionSpinner = findViewById(R.id.spinner_condition)
//        adTitle = findViewById(R.id.et_ad_title)
//        adDescription = findViewById(R.id.et_ad_description)
//        adPrice = findViewById(R.id.et_ad_price)
//        phoneNumberSwitch = findViewById(R.id.switch_phone_number)
//        postButton = findViewById(R.id.post_btn)
//        addImage = findViewById(R.id.add_image)
//
//        dbHelper = DBHelper(this)
//
//        checkStoragePermission()
//        setupSpinners()
//
//        addImage.setOnClickListener {
//            pickImageFromGallery()
//        }
//
//        postButton.setOnClickListener {
//            submitAd()
//        }
//    }
//
//    private fun setupSpinners() {
//        val categories = arrayOf("Mobile", "Car", "Laptop", "Bikes")
//        val conditions = arrayOf("Used", "New", "Refurbished")
//
//        categorySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
//        conditionSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, conditions)
//    }
//
//    private fun pickImageFromGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, IMAGE_PICK_CODE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
//            selectedImageUri = data?.data
//        }
//    }
//
//    private fun checkStoragePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                STORAGE_PERMISSION_CODE
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun submitAd() {
//        val category = categorySpinner.selectedItem.toString()
//        val condition = conditionSpinner.selectedItem.toString()
//        val title = adTitle.text.toString()
//        val description = adDescription.text.toString()
//        val price = adPrice.text.toString()
//        val showPhoneNumber = phoneNumberSwitch.isChecked
//        val uId= dbHelper.getUserId()
//        val ad = Ad( id =0,userId=uId, imageUrl = " ", category=category, condition = condition, title = title, description = description, price = price, showPhoneNumber = showPhoneNumber)
//        Log.d("AdDetails", ad.toString())
//
//        selectedImageUri?.let { uri ->
//            val filePath = getFileFromUri(uri)
//            filePath?.let { file ->
//                sendToServer(ad, file)
//            }
//        } ?: run {
//            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun getFileFromUri(uri: Uri): File? {
//        return try {
//            val inputStream = contentResolver.openInputStream(uri)
//            val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
//            inputStream?.use { input ->
//                tempFile.outputStream().use { output ->
//                    input.copyTo(output)
//                }
//            }
//            tempFile
//        } catch (e: Exception) {
//            Log.e("FileError", "Failed to get file from URI", e)
//            null
//        }
//    }
//
//    private fun sendToServer(ad: Ad, imageFile: File) {
//        val adJson = Gson().toJson(ad)
//        val adRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), adJson)
//
//        val imageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
//        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
//
//        lifecycleScope.launch {
//            val response = RetrofitInstance.api.postAd(adRequestBody, imagePart)
//            if (response.isSuccessful) {
//                Toast.makeText(this@SellActivity, "Ad posted successfully!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@SellActivity, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            } else {
//                Toast.makeText(this@SellActivity, "Failed to post ad", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}
//
