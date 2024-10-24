package ModelClass

import android.location.Address

data class Facility(
    val clinic_id: String,
    val name: String,
    val description: String,
    var imageUrl: String,  // URL of the image from Firebase Storage
    var email: String,
    var phoneNumber: String,
    var address: String,
    val rating: Float,
    var isFavorite: Boolean
)
