package com.example.olx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class AccountFragment : Fragment() {

    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        dbHelper = DBHelper(requireContext())

        val (userEmail, isLoggedIn) = dbHelper.getUser()

        Log.d("AccountFragment", "isLoggedIn: $isLoggedIn")


        val user_name: TextView =
            view.findViewById(R.id.user_name)
//             Find the TextView using its ID
            val textViewButton_viewEditProfile: TextView =
                view.findViewById(R.id.btn_view_edit_profile)

            // Set OnClickListener for the TextView
            textViewButton_viewEditProfile.setOnClickListener {
                // Start the new activity when the TextView is clicked
                val intent = Intent(requireContext(), ProfileActivity::class.java)
                startActivity(intent)
            }

            val Button_publicProfileProfile: ConstraintLayout =
                view.findViewById(R.id.publicProfileButton)

            // Set OnClickListener for the TextView
            Button_publicProfileProfile.setOnClickListener {
                // Start the new activity when the TextView is clicked
                val intent = Intent(requireContext(), PublicProfileActivity::class.java)
                startActivity(intent)
            }

//            val Button_Favorites: ConstraintLayout = view.findViewById(R.id.favoritesButton)
//
//            // Set OnClickListener for the TextView
//            Button_Favorites.setOnClickListener {
//                // Start the new activity when the TextView is clicked
//                val intent = Intent(requireContext(), FavoritesActivity::class.java)
//                startActivity(intent)
//            }

            val Button_Settings: ConstraintLayout = view.findViewById(R.id.setting_Button)

            // Set OnClickListener for the TextView
            Button_Settings.setOnClickListener {
                // Start the new activity when the TextView is clicked
                val intent = Intent(requireContext(), SettingsActivity::class.java)
                startActivity(intent)
            }


        val authButton: TextView = view.findViewById(R.id.btn_auth) // Ensure btn_auth is a TextView

        if (isLoggedIn) {
            user_name.text= userEmail
            authButton.text = "Logout" // Set text to "Logout" if user is logged in

            authButton.setOnClickListener {
                dbHelper.clearUser()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            authButton.text = "Login" // Set text to "Login" if user is not logged in
            user_name.text= "Login"
            textViewButton_viewEditProfile.text= "Login to Your Account"

            textViewButton_viewEditProfile.setOnClickListener {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            authButton.setOnClickListener {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }


        return view
    }
}

//
//
//package com.example.olx
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.fragment.app.Fragment
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//class AccountFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_account, container, false)
//
//        val sharedPreferences =
//            requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
//        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
//
//        if (isLoggedIn) {
//            // Display the logged-in UI
//            val layoutLoggedIn = view.findViewById<ConstraintLayout>(R.id.logged_in_layout)
//            layoutLoggedIn.visibility = View.VISIBLE
//
//            val logoutButton: Button = view.findViewById(R.id.btn_logout)
//            logoutButton.setOnClickListener {
//                // Clear session and navigate to Login screen
//                sharedPreferences.edit().clear().apply()
//                val intent = Intent(requireContext(), LoginActivity::class.java)
//                startActivity(intent)
//                requireActivity().finish()
//            }
//
//            // Find the TextView using its ID
//            val textViewButton_viewEditProfile: TextView =
//                view.findViewById(R.id.btn_view_edit_profile)
//
//            // Set OnClickListener for the TextView
//            textViewButton_viewEditProfile.setOnClickListener {
//                // Start the new activity when the TextView is clicked
//                val intent = Intent(requireContext(), ProfileActivity::class.java)
//                startActivity(intent)
//            }
//
//            val Button_publicProfileProfile: ConstraintLayout =
//                view.findViewById(R.id.publicProfileButton)
//
//            // Set OnClickListener for the TextView
//            Button_publicProfileProfile.setOnClickListener {
//                // Start the new activity when the TextView is clicked
//                val intent = Intent(requireContext(), PublicProfileActivity::class.java)
//                startActivity(intent)
//            }
//
//            val Button_Favorites: ConstraintLayout = view.findViewById(R.id.favoritesButton)
//
//            // Set OnClickListener for the TextView
//            Button_Favorites.setOnClickListener {
//                // Start the new activity when the TextView is clicked
//                val intent = Intent(requireContext(), FavoritesActivity::class.java)
//                startActivity(intent)
//            }
//
//            val Button_Settings: ConstraintLayout = view.findViewById(R.id.setting_Button)
//
//            // Set OnClickListener for the TextView
//            Button_Settings.setOnClickListener {
//                // Start the new activity when the TextView is clicked
//                val intent = Intent(requireContext(), SettingsActivity::class.java)
//                startActivity(intent)
//            }
//        } else {
//            // Display the login button
//            val loginButton: Button = view.findViewById(R.id.login_button)
//            loginButton.visibility = View.VISIBLE
//            loginButton.setOnClickListener {
//
//                val intent = Intent(requireContext(), LoginActivity::class.java)
//                startActivity(intent)
//                requireActivity().finish()
//
//            }
//
//
//        }
//
//    return view
//}
//}
//
