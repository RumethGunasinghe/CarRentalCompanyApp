package com.example.carrentalapp

import Car
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class BookingAdapter(private val bookedCars: MutableList<Car>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCar: ImageView = itemView.findViewById(R.id.imageCar)
        val tvName: TextView = itemView.findViewById(R.id.textCarName)
        val tvInfo: TextView = itemView.findViewById(R.id.textCarType)
        val tvCost: TextView = itemView.findViewById(R.id.textCarPrice)
        val btnCancelRent: Button = itemView.findViewById(R.id.btnCancelRent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car_card, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val car = bookedCars[position]

        holder.ivCar.setImageResource(car.imageResId)
        holder.tvName.text = car.name
        holder.tvInfo.text = "${car.year}, ${car.model}, Rating: ${car.rating}/5"
        holder.tvCost.text = "Daily Cost: ${car.dailyCost} credits"

        holder.btnCancelRent.visibility = if (car.rented) View.VISIBLE else View.GONE

        holder.btnCancelRent.setOnClickListener {
            car.rented = false
            CarRepository.creditBalance += car.dailyCost // optional refund

            // Remove from bookedCars list
            bookedCars.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, bookedCars.size)

            Toast.makeText(
                holder.itemView.context,
                "Cancelled rental for ${car.name}.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = bookedCars.size
}
