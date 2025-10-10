package com.example.carrentalapp

import Car
import com.example.carrentalapp.CarRepository
import android.content.Intent
import android.os.Bundle
import android.widget.*
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var carImage: ImageView
    private lateinit var carDetails: TextView
    private lateinit var credits: TextView
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var btnDetails: Button
    private lateinit var ivFavourite: ImageView


    private lateinit var availableCars: List<Car> // Only cars not rented
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1️⃣ Get stored dark mode preference
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("DARK_MODE", false)

        // 2️⃣ Apply theme BEFORE loading the layout
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        setContentView(R.layout.activity_main)

        val switchDarkMode = findViewById<Switch>(R.id.switchDarkMode)
        switchDarkMode.isChecked = isDarkMode

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("DARK_MODE", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        val btnSort: Button = findViewById(R.id.btnSort)
        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Called when user presses Enter/Search
                if (!query.isNullOrEmpty()) {
                    performSearch(query)
                } else {
                    // If empty query, reset to show all cars
                    availableCars = getAvailableCars()
                    currentIndex = 0
                    displayCar(currentIndex)
                }
                searchView.clearFocus() // hides keyboard after search
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optional: live filtering (we can skip this)
                return false
            }
        })


        btnSort.setOnClickListener {
            val popup = PopupMenu(this, btnSort)
            popup.menuInflater.inflate(R.menu.sort_menu, popup.menu)

            popup.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.sort_rating -> {
                        availableCars = availableCars.sortedByDescending { it.rating }
                        currentIndex = 0
                        displayCar(currentIndex)
                        true
                    }
                    R.id.sort_year -> {
                        availableCars = availableCars.sortedByDescending { it.year }
                        currentIndex = 0
                        displayCar(currentIndex)
                        true
                    }
                    R.id.sort_cost -> {
                        availableCars = availableCars.sortedBy { it.dailyCost }
                        currentIndex = 0
                        displayCar(currentIndex)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }



        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> true // Already here
                R.id.favourites -> {
                    startActivity(Intent(this, Favourites::class.java))
                    true
                }
                R.id.bookings -> {
                    startActivity(Intent(this, Bookings::class.java))
                    true
                }
                else -> false
            }
        }


        carImage = findViewById(R.id.imageView)
        ivFavourite = findViewById(R.id.ivFavourite)
        carDetails = findViewById(R.id.textView3)
        credits = findViewById(R.id.credits)
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnDetails = findViewById(R.id.btnDetails)

        credits.text = "Credits: ${CarRepository.creditBalance}"


        availableCars = getAvailableCars()
        if (availableCars.isNotEmpty()) displayCar(currentIndex)

        // --- Favourite toggle ---
        ivFavourite.setOnClickListener {
            val car = availableCars[currentIndex]
            car.isFavourite = !car.isFavourite
            updateFavouriteIcon(car)

            if (car.isFavourite) {
                if (!CarRepository.favourites.contains(car))
                    CarRepository.favourites.add(car)
                Toast.makeText(this, "${car.name} added to favourites!", Toast.LENGTH_SHORT).show()
            } else {
                CarRepository.favourites.remove(car)
                Toast.makeText(this, "${car.name} removed from favourites!", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Next / Previous ---
        btnNext.setOnClickListener {
            if (availableCars.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % availableCars.size
                displayCar(currentIndex)
            }
        }

        btnPrevious.setOnClickListener {
            if (availableCars.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) availableCars.size - 1 else currentIndex - 1
                displayCar(currentIndex)
            }
        }

        // --- Details / Rent ---
        btnDetails.setOnClickListener {
            val intent = Intent(this, CarDetails::class.java)
            intent.putExtra("CAR_ID", CarRepository.availableCars.indexOf(availableCars[currentIndex]))
            startActivity(intent)
        }
    }

    private fun performSearch(query: String) {
        val filteredCars = getAvailableCars().filter { car ->
            car.name.contains(query, ignoreCase = true) ||
                    car.model.contains(query, ignoreCase = true) || car.year.contains(query, ignoreCase = true)
        }

        if (filteredCars.isNotEmpty()) {
            availableCars = filteredCars
            currentIndex = 0
            displayCar(currentIndex)
            Toast.makeText(this, "${filteredCars.size} car(s) found", Toast.LENGTH_SHORT).show()
        } else {
            carDetails.text = "No cars found for \"$query\""
            carImage.setImageResource(0)
        }
    }


    private fun updateFavouriteIcon(car: Car) {
        if (car.isFavourite) {
            ivFavourite.setImageResource(R.drawable.ic_favadded)
        } else {
            ivFavourite.setImageResource(R.drawable.ic_nonfav)
        }
    }

    private fun displayCar(index: Int) {
        val car = availableCars[index]
        carImage.setImageResource(car.imageResId)
        carDetails.text = """
            Name: ${car.name}
            Model: ${car.model}
            Year: ${car.year}
            Rating: ${car.rating} / 5
            Kilometres: ${car.kilometres}
            Daily Cost: ${car.dailyCost} credits
        """.trimIndent()

        updateFavouriteIcon(car)
    }

    private fun getAvailableCars(): List<Car> {
        return CarRepository.availableCars.filter { !it.rented } // only not rented
    }

    override fun onResume() {
        super.onResume()
        // Refresh list after returning from CarDetails
        availableCars = getAvailableCars()
        if (availableCars.isNotEmpty()) {
            if (currentIndex >= availableCars.size) currentIndex = 0
            displayCar(currentIndex)
        } else {
            carDetails.text = "No cars available!"

        }
        credits.text = "Credits: ${CarRepository.creditBalance}"
    }
}
