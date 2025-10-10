package com.example.carrentalapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Favourites : AppCompatActivity() {

    private lateinit var rvFavourites: RecyclerView
    private lateinit var adapter: FavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("DARK_MODE", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.favourites -> {
                    true
                }
                R.id.bookings -> {
                    startActivity(Intent(this, Bookings::class.java))
                    true
                }
                else -> false
            }
        }

        rvFavourites = findViewById(R.id.rvFavourites)
        rvFavourites.layoutManager = LinearLayoutManager(this)

        adapter = FavouriteAdapter(CarRepository.favourites)
        rvFavourites.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged() // Refresh list if favourites changed
    }
}
