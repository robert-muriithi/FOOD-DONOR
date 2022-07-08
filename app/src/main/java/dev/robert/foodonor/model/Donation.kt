package dev.robert.foodonor.model

import com.google.firebase.firestore.GeoPoint

data class Donation(
    var id: String? = "",
    var name: String? = "",
    var foodItem: String? = "",
    var phoneNumber: String? = "",
    var description: String? = "",
    var location: GeoPoint? = null
)
