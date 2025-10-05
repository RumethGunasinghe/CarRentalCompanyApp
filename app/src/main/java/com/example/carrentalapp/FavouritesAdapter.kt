package com.example.carrentalapp

import Car
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavouriteAdapter(private val favouriteCars: List<Car>) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCarImage: ImageView = itemView.findViewById(R.id.ivCarImage)
        val tvCarName: TextView = itemView.findViewById(R.id.tvCarName)
        val tvCarDetails: TextView = itemView.findViewById(R.id.tvCarDetails)
        val tvDailyCost: TextView = itemView.findViewById(R.id.tvDailyCost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite_car, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val car = favouriteCars[position]
        holder.ivCarImage.setImageResource(car.imageResId)
        holder.tvCarName.text = car.name
        holder.tvCarDetails.text = "${car.year}, ${car.model}, Rating: ${car.rating}/5"
        holder.tvDailyCost.text = "Daily Cost: ${car.dailyCost} credits"
    }

    override fun getItemCount(): Int = favouriteCars.size
}
