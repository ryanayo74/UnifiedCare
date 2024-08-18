package ModelClass

data class Facility(
    val name: String,
    val description: String,
    val imageResId: Int,
    val rating: Float,
    var isFavorite: Boolean
)