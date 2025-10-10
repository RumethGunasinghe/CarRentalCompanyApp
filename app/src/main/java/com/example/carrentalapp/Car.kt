
data class Car(
    val id : Int,
    val name: String,
    val model: String,
    val year: String,
    val rating: Float,
    val kilometres: Int,
    val dailyCost: Int,
    val imageResId: Int,
    val description : String = " ",
    var isFavourite: Boolean = false,
    var rented : Boolean= false
)
