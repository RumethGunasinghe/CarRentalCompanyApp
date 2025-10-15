package com.example.carrentalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Favourites : AppCompatActivity() {

    private lateinit var rvFavourites: RecyclerView
    private lateinit var adapter: FavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("FavouritesActivity", "onCreate called")

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("DARK_MODE", false)
        Log.d("FavouritesActivity", "Dark mode: $isDarkMode")

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        Log.d("FavouritesActivity", "Layout set successfully")

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    Log.d("FavouritesActivity", "Navigating to Home")
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.favourites -> {
                    Log.d("FavouritesActivity", "Already in Favourites")
                    true
                }
                R.id.bookings -> {
                    Log.d("FavouritesActivity", "Navigating to Bookings")
                    startActivity(Intent(this, Bookings::class.java))
                    true
                }
                else -> false
            }
        }

        rvFavourites = findViewById(R.id.rvFavourites)
        rvFavourites.layoutManager = LinearLayoutManager(this)
        Log.d("FavouritesActivity", "RecyclerView initialized")

        adapter = FavouriteAdapter(CarRepository.favourites)
        rvFavourites.adapter = adapter
        Log.d("FavouritesActivity", "Adapter set with ${CarRepository.favourites.size} favourites")
    }

    override fun onResume() {
        super.onResume()
        Log.d("FavouritesActivity", "onResume called - refreshing adapter")
        adapter.notifyDataSetChanged() // Refresh list if favourites changed
    }
}
