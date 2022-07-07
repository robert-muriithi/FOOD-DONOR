package dev.robert.foodonor.repository

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
    override suspend fun getDonations(result: (Resource<List<Donation>>) -> Unit) {
        database.getReference("Donations").addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                result(Resource.Error(p0.message))
            }
            override fun onDataChange(p0: DataSnapshot) {
                val donations = p0.children.mapNotNull {
                    it.getValue(Donation::class.java)
                }
                result(Resource.Success(donations))
            }
        })

    }
}