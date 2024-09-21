package ModelClass

data class Facility(
    val name: String,
    val description: String,
    val imageUrl: String,  // URL of the image from Firebase Storage
    val rating: Float,
    var isFavorite: Boolean
)
