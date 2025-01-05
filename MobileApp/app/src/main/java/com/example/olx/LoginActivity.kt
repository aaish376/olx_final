package com.example.olx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import retrofit2.Response
import retrofit2.HttpException
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        val emailEt: EditText = findViewById(R.id.emailEt)
        val passET: EditText = findViewById(R.id.passET)
        val loginButton: Button = findViewById(R.id.button)

        loginButton.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val password = passET.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authenticateUser(email, password)
        }


        val btn_register: TextView = findViewById(R.id.btn_register)
        btn_register.setOnClickListener {
            // Start the new activity when the TextView is clicked
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun authenticateUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        lifecycleScope.launch {
            try {
                val response: Response<LoginResponse> = RetrofitInstance.api.login(loginRequest)

                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    if (loginResponse != null ) {
                        Log.d("userId", "${loginResponse.userId}")

                        dbHelper.saveUser(loginResponse.userId ,email, true) // Save user to DB
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Failed to connect to server", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
