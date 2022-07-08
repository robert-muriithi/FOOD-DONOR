package dev.robert.foodonor.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.robert.foodonor.model.Donation
import dev.robert.foodonor.utils.Resource
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : MainRepository {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser?.uid
    override suspend fun getDonations(result: (Resource<List<Donation>>) -> Unit) {
        database.getReference("Donations").child(user.toString()).addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                result(Resource.Error(p0.message))
            }
            override fun onDataChange(p0: DataSnapshot) {
                val donations = ArrayList<Donation>()
                database.getReference("Donations")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        for (data in snapshot.children) {
                            val donation = data.getValue(Donation::class.java)
                            donations.add(donation!!)
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
        })

    }
}