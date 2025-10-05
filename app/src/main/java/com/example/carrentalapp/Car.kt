
data class Car(
    val id : Int,
    val name: String,
    val model: String,
    val year: Int,
    val rating: Float,
    val kilometres: Int,
    val dailyCost: Int,
    val imageResId: Int,
    var isFavourite: Boolean = false,
    var rented : Boolean= false
)
