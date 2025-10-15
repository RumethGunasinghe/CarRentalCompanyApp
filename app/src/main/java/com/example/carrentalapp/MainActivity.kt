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
import android.util.Log

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var carImage: ImageView
    private lateinit var carDetails: TextView
    private lateinit var credits: TextView
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var btnDetails: Button
    private lateinit var ivFavourite: ImageView

    private lateinit var availableCars: List<Car>
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called")

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("DARK_MODE", false)
        Log.d(TAG, "Dark mode preference: $isDarkMode")

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        setContentView(R.layout.activity_main)
        Log.d(TAG, "Layout set")

        val switchDarkMode = findViewById<Switch>(R.id.switchDarkMode)
        switchDarkMode.isChecked = isDarkMode
        Log.d(TAG, "Dark mode switch initialized")

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("DARK_MODE", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            Log.d(TAG, "Dark mode toggled: $isChecked")
        }

        val btnSort: Button = findViewById(R.id.btnSort)
        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "Search submitted: $query")
                if (!query.isNullOrEmpty()) {
                    performSearch(query)
                } else {
                    availableCars = getAvailableCars()
                    currentIndex = 0
                    displayCar(currentIndex)
                    Log.d(TAG, "Search cleared, showing all cars")
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        btnSort.setOnClickListener {
            Log.d(TAG, "Sort button clicked")
            val popup = PopupMenu(this, btnSort)
            popup.menuInflater.inflate(R.menu.sort_menu, popup.menu)

            popup.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.sort_rating -> {
                        Log.d(TAG, "Sorting by rating")
                        availableCars = availableCars.sortedByDescending { it.rating }
                        currentIndex = 0
                        displayCar(currentIndex)
                        true
                    }
                    R.id.sort_year -> {
                        Log.d(TAG, "Sorting by year")
                        availableCars = availableCars.sortedByDescending { it.year }
                        currentIndex = 0
                        displayCar(currentIndex)
                        true
                    }
                    R.id.sort_cost -> {
                        Log.d(TAG, "Sorting by cost")
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
            Log.d(TAG, "Bottom navigation clicked: ${item.title}")
            when (item.itemId) {
                R.id.home -> true
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
        Log.d(TAG, "UI components initialized")

        credits.text = "Credits: ${CarRepository.creditBalance}"
        Log.d(TAG, "Initial credits: ${CarRepository.creditBalance}")

        availableCars = getAvailableCars()
        Log.d(TAG, "Available cars loaded: ${availableCars.size}")
        if (availableCars.isNotEmpty()) displayCar(currentIndex)

        ivFavourite.setOnClickListener {
            val car = availableCars[currentIndex]
            car.isFavourite = !car.isFavourite
            updateFavouriteIcon(car)
            Log.d(TAG, "Favourite toggled for car: ${car.name}, now: ${car.isFavourite}")

            if (car.isFavourite) {
                if (!CarRepository.favourites.contains(car))
                    CarRepository.favourites.add(car)
                Toast.makeText(this, "${car.name} added to favourites!", Toast.LENGTH_SHORT).show()
            } else {
                CarRepository.favourites.remove(car)
                Toast.makeText(this, "${car.name} removed from favourites!", Toast.LENGTH_SHORT).show()
            }
        }

        btnNext.setOnClickListener {
            if (availableCars.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % availableCars.size
                displayCar(currentIndex)
                Log.d(TAG, "Next button clicked, showing index: $currentIndex")
            }
        }

        btnPrevious.setOnClickListener {
            if (availableCars.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) availableCars.size - 1 else currentIndex - 1
                displayCar(currentIndex)
                Log.d(TAG, "Previous button clicked, showing index: $currentIndex")
            }
        }

        btnDetails.setOnClickListener {
            val intent = Intent(this, CarDetails::class.java)
            val carIndex = CarRepository.availableCars.indexOf(availableCars[currentIndex])
            intent.putExtra("CAR_ID", carIndex)
            Log.d(TAG, "Opening CarDetails for car index: $carIndex")
            startActivity(intent)
        }
    }

    private fun performSearch(query: String) {
        Log.d(TAG, "performSearch() called with query: $query")
        val filteredCars = getAvailableCars().filter { car ->
            car.name.contains(query, ignoreCase = true) ||
                    car.model.contains(query, ignoreCase = true) || car.year.contains(query, ignoreCase = true)
        }
        Log.d(TAG, "Search results: ${filteredCars.size} cars")

        if (filteredCars.isNotEmpty()) {
            availableCars = filteredCars
            currentIndex = 0
            displayCar(currentIndex)
        } else {
            carDetails.text = "No cars found for \"$query\""
            carImage.setImageResource(0)
        }
    }

    private fun updateFavouriteIcon(car: Car) {
        ivFavourite.setImageResource(
            if (car.isFavourite) R.drawable.ic_favadded else R.drawable.ic_nonfav
        )
        Log.d(TAG, "Updated favourite icon for ${car.name}")
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
        Log.d(TAG, "Displayed car: ${car.name} (index $index)")
    }

    private fun getAvailableCars(): List<Car> {
        val cars = CarRepository.availableCars.filter { !it.rented }
        Log.d(TAG, "getAvailableCars() returned ${cars.size} cars")
        return cars
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called — refreshing car list")
        availableCars = getAvailableCars()
        if (availableCars.isNotEmpty()) {
            if (currentIndex >= availableCars.size) currentIndex = 0
            displayCar(currentIndex)
        } else {
            carDetails.text = "No cars available!"
            Log.w(TAG, "No cars available to display")
        }
        credits.text = "Credits: ${CarRepository.creditBalance}"
        Log.d(TAG, "Credits updated: ${CarRepository.creditBalance}")
    }
}
