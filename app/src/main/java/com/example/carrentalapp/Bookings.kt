package com.example.carrentalapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Bookings : AppCompatActivity() {

    private lateinit var rvBookings: RecyclerView
    private lateinit var adapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.favourites -> {
                    startActivity(Intent(this, Favourites::class.java))
                    true
                }
                R.id.bookings -> true
                else -> false
            }
        }

        rvBookings = findViewById(R.id.rvBookings)
        rvBookings.layoutManager = LinearLayoutManager(this)

        // Show only rented cars
        val bookedCars = CarRepository.availableCars.filter { it.rented }

        adapter = BookingAdapter(bookedCars)
        rvBookings.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
