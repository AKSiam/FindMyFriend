package com.example.findmyfriend.View
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.findmyfriend.R
import com.example.findmyfriend.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var actionDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentContainerView)
        binding.bottomBar.setupWithNavController(navController)
        binding.drawerNav.setupWithNavController(navController)

        actionDrawerToggle = ActionBarDrawerToggle(
            this, binding.main, R.string.nav_open, R.string.nav_close
        )
        actionDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.drawerNav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    Firebase.auth.signOut()
                    startActivity(
                        Intent(this, LoginActivity::class.java)
                    )
                    finish()
                }
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)

                }
                R.id.friends -> {
                    navController.navigate(R.id.friendsFragment)
                }

            }
            true
        }
        binding.bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    Firebase.auth.signOut()
                    startActivity(
                        Intent(this, LoginActivity::class.java)
                    )
                    finish()
                }
                R.id.friends -> {
                    navController.navigate(R.id.friendsFragment)
                }
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                }

            }
            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}