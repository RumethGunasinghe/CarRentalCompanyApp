package com.example.carrentalapp

import Car
import CarRepository
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CarDetails : AppCompatActivity() {

    private lateinit var ivCarImage: ImageView
    private lateinit var tvCarName: TextView
    private lateinit var tvCarInfo: TextView
    private lateinit var tvCarCost: TextView
    private lateinit var tvBtnBack: Button
    private lateinit var tvBtnRent: Button
    private lateinit var seekBarDays: SeekBar
    private lateinit var tvDaysSelected: TextView

    private var carId: Int = -1
    private lateinit var car: Car
    private var selectedDays: Int = 1  // default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        // --- Bottom Nav ---
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

        // --- Views ---
        ivCarImage = findViewById(R.id.ivCarDetailImage)
        tvCarName = findViewById(R.id.tvCarDetailName)
        tvCarInfo = findViewById(R.id.tvCarDetailInfo)
        tvCarCost = findViewById(R.id.tvCarDetailCost)
        tvBtnBack = findViewById(R.id.btnBack)
        tvBtnRent = findViewById(R.id.btnRent)
        seekBarDays = findViewById(R.id.seekBar)
        tvDaysSelected = findViewById(R.id.tvDaysSelected)

        // --- Load car ---
        carId = intent.getIntExtra("CAR_ID", -1)
        car = CarRepository.availableCars[carId] ?: return

        ivCarImage.setImageResource(car.imageResId)
        tvCarName.text = car.name
        tvCarInfo.text = "${car.year}, ${car.model}, Rating: ${car.rating}/5"
        tvCarCost.text = "Daily Cost: ${car.dailyCost} credits"

        // --- SeekBar logic ---
        seekBarDays.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedDays = progress + 1 // 0..6 → 1..7
                tvDaysSelected.text = "Days: $selectedDays"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // --- Back button ---
        tvBtnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // --- Rent button ---
        tvBtnRent.setOnClickListener {
            val totalCost = selectedDays * car.dailyCost

            if (totalCost > CarRepository.creditBalance) {
                Toast.makeText(this, "Not enough credits! You have ${CarRepository.creditBalance}", Toast.LENGTH_SHORT).show()
            } else {
                CarRepository.creditBalance -= totalCost
                CarRepository.availableCars[carId].rented = true
                Toast.makeText(
                    this,
                    "Rented ${car.name} for $selectedDays days. Total cost: $totalCost credits. Remaining: ${CarRepository.creditBalance}",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
