package com.example.carrentalapp

import Car
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var carImage: ImageView
    private lateinit var carDetails: TextView
    private lateinit var credits: TextView
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var btnDetails: Button
    private lateinit var ivFavourite : ImageView

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Already in MainActivity, do nothing
                    true
                }
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

        // Find views
        carImage = findViewById(R.id.imageView)
        ivFavourite = findViewById(R.id.ivFavourite)
        carDetails = findViewById(R.id.textView3)
        credits = findViewById(R.id.credits)
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnDetails = findViewById(R.id.btnDetails)

        credits.text = "Credits: ${CarRepository.creditBalance}"

        // Display the first car
        displayCar(currentIndex)

        ivFavourite.setOnClickListener {
            val car = CarRepository.availableCars[currentIndex]
            car.isFavourite = !car.isFavourite
            updateFavouriteIcon(car)

            // Optional: add/remove from favourites list
            if (car.isFavourite) {
                CarRepository.favourites.add(car)
                Toast.makeText(this, "${car.name} added to favourites!", Toast.LENGTH_SHORT).show()
            } else {
                CarRepository.favourites.remove(car)
                Toast.makeText(this, "${car.name} removed from favourites!", Toast.LENGTH_SHORT).show()
            }
        }


        // Next button
        btnNext.setOnClickListener {
            currentIndex = (currentIndex + 1) % CarRepository.availableCars.size
            displayCar(currentIndex)
        }

        // Previous button
        btnPrevious.setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) CarRepository.availableCars.size - 1 else currentIndex - 1
            displayCar(currentIndex)
        }

        // Details / Rent button
        btnDetails.setOnClickListener {
            val car = CarRepository.availableCars[currentIndex]
            if (CarRepository.creditBalance >= car.dailyCost) {
                CarRepository.creditBalance -= car.dailyCost
                CarRepository.rentedCars.add(car)
                credits.text = "Credits: ${CarRepository.creditBalance}"
                Toast.makeText(this, "You rented ${car.name}!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not enough credits!", Toast.LENGTH_SHORT).show()
            }
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
        val car = CarRepository.availableCars[index]
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


}
