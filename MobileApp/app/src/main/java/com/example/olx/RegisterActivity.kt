package com.example.olx

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Update with the actual XML file name

        val unameEt: EditText = findViewById(R.id.unameEt)
        val emailET: EditText = findViewById(R.id.emailET)
        val passET: EditText = findViewById(R.id.passET)
        val cpassET: EditText = findViewById(R.id.cpassET)
        val registerButton: TextView = findViewById(R.id.button)
        val loginLink: TextView = findViewById(R.id.btnn_login)

        registerButton.setOnClickListener {
            val username = unameEt.text.toString().trim()
            val email = emailET.text.toString().trim()
            val password = passET.text.toString().trim()
            val confirmPassword = cpassET.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call API to register the user
            registerUser(username, email, password)
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        val registerRequest = RegisterRequest(username, email, password)

        lifecycleScope.launch {
            try {
                val response: Response<Boolean> = RetrofitInstance.api.register(registerRequest)
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null ) {
                        Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity,  "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Failed to connect to server: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
