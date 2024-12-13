package com.example.aiventure

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle home item click
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_camera -> {
                    // Handle dashboard item click
                    navController.navigate(R.id.navigation_camera)
                    true
                }
                R.id.nav_booking -> {
                    navController.navigate(R.id.navigation_booking)
                    true
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                R.id.nav_category -> {
                    navController.navigate(R.id.navigation_category)
                    true
                }
                else -> false
            }
        }
    }
}