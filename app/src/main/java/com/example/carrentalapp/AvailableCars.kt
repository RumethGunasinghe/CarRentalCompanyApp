import com.example.carrentalapp.R

// CarRepository.kt
object CarRepository {
    val availableCars = mutableListOf<Car>()
    val rentedCars = mutableListOf<Car>()
    val favourites = mutableListOf<Car>()

    var creditBalance = 500

    // Initialize some cars
    init {
        availableCars.add(Car(0,"Toyota", "Corolla", 2020, 4.5f, 30000, 150, R.drawable.car1))
        availableCars.add(Car(1, "Honda", "Civic", 2019, 4.0f, 25000, 120, R.drawable.car2))
        availableCars.add(Car(2, "Ford", "Mustang", 2022, 4.8f, 10000, 350, R.drawable.car3))
        availableCars.add(Car(3, "BMW", "X5", 2021, 4.7f, 15000, 400, R.drawable.car4))
        availableCars.add(Car(4, "Audi", "A4", 2020, 4.3f, 20000, 300, R.drawable.car5))
    }
}
