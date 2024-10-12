package ModelClass

import android.location.Address

data class Facility(
    val name: String,
    val description: String,
    val imageUrl: String,  // URL of the image from Firebase Storage
    val email: String,
    val phoneNumber: String,
    val address: String,
    val rating: Float,
    var isFavorite: Boolean
)
