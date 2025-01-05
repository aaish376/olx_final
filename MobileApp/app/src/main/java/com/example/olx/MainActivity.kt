//
//
//package com.example.olx
//
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var bottomNavigationView: BottomNavigationView
//    private lateinit var floatingActionButton: FloatingActionButton
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        bottomNavigationView = findViewById(R.id.bottomNv)
//        floatingActionButton = findViewById(R.id.sellFab)
//
//
//
//        // Handle BottomNavigationView item selection
//        bottomNavigationView.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.nav_home -> {
//                    replaceFragment(HomeFragment())
//                    true
//                }
//                R.id.nav_chats -> {
//                    replaceFragment(ChatFragment())
//                    true
//                }
//                R.id.nav_my_ads -> {
//                    replaceFragment(MyAdFragment())
//                    true
//                }
//                R.id.nav_account -> {
//                    replaceFragment(AccountFragment())
//                    true
//                }
//                else -> false
//            }
//        }
//
//        // Set OnClickListener for the FloatingActionButton
//        floatingActionButton.setOnClickListener {
//            // Navigate to SellPageActivity on FAB click
//            val intent = Intent(this, SellActivity::class.java)
//            startActivity(intent)
//        }
//
//
//        // Load the default fragment
//        replaceFragment(HomeFragment())
//    }
//
//
//
//    // Function to replace fragments
//    private fun replaceFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
//    }
//}
//

package com.example.olx



import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var floatingActionButton: FloatingActionButton

    companion object {
        private const val REQUEST_CODE_SELL_ACTIVITY = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNv)
        floatingActionButton = findViewById(R.id.sellFab)

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_chats -> {
                    replaceFragment(ChatFragment())
                    true
                }
                R.id.nav_my_ads -> {
                    replaceFragment(MyAdFragment())
                    true
                }
                R.id.nav_account -> {
                    replaceFragment(AccountFragment())
                    true
                }
                else -> false
            }
        }

        // Set OnClickListener for the FloatingActionButton
        floatingActionButton.setOnClickListener {
            // Navigate to SellActivity with a request code
            val intent = Intent(this, SellActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SELL_ACTIVITY)
        }

        // Load the default fragment
        replaceFragment(HomeFragment())
    }

    // Function to replace fragments
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }

    // Handle the result from SellActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELL_ACTIVITY && resultCode == RESULT_OK) {
            // Show HomeFragment after successful ad posting
            replaceFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }
}
