package dev.robert.foodonor.model

import com.google.firebase.firestore.GeoPoint

data class Donation(
    val id: String = "",
    val name: String = "",
    val foodItem: String = "",
    val phoneNumber: String = "",
    val description: String = "",
    val location: GeoPoint
)
