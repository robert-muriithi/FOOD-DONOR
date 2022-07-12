package dev.robert.foodonor.repository

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.foodonor.model.Donation
import dev.robert.foodonor.utils.Resource
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore
) : MainRepository {

    override suspend fun getDonations(result: (Resource<List<Donation>>) -> Unit) {

        database.collection("Donations")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList<Donation>()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Resource.Success(donations)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }

    }

    override suspend fun donate(donation: Donation, result: (Resource<List<Donation>>) -> Unit) {
        database.collection("Donations")
            .add(donation)
            .addOnSuccessListener {
                result.invoke(
                    Resource.Success(arrayListOf(donation))
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }


}