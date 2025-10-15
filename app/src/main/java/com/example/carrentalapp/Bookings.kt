package com.example.carrentalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Bookings : AppCompatActivity() {

    private val TAG = "BookingsActivity"

    private lateinit var rvBookings: RecyclerView
    private lateinit var adapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("DARK_MODE", false)
        Log.d(TAG, "Dark mode: $isDarkMode")

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)
        Log.d(TAG, "Layout set successfully")

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    Log.d(TAG, "Navigating to Home")
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.favourites -> {
                    Log.d(TAG, "Navigating to Favourites")
                    startActivity(Intent(this, Favourites::class.java))
                    true
                }
                R.id.bookings -> {
                    Log.d(TAG, "Already in Bookings")
                    true
                }
                else -> false
            }
        }

        rvBookings = findViewById(R.id.rvBookings)
        rvBookings.layoutManager = LinearLayoutManager(this)
        Log.d(TAG, "RecyclerView initialized")

        val bookedCars = CarRepository.availableCars.filter { it.rented }.toMutableList()
        Log.d(TAG, "Booked cars count: ${bookedCars.size}")

        adapter = BookingAdapter(bookedCars)
        rvBookings.adapter = adapter
        Log.d(TAG, "Adapter set for booked cars")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called - refreshing adapter")
        adapter.notifyDataSetChanged()
    }
}
