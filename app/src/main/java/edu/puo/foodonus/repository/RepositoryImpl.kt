package edu.puo.foodonus.repository

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.puo.foodonus.model.Donation
import edu.puo.foodonus.model.User
import edu.puo.foodonus.utils.Resource
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : MainRepository {
    private val TAG = "RepositoryImpl"


    override suspend fun getDonations(result: (Resource<List<Donation>>) -> Unit) {

        database.collection("donations")
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
        val donationId = donation.donorId!!
        database.collection("donations").document(donationId).set(donation)
            .addOnSuccessListener {
                result.invoke(
                    Resource.Success(listOf(donation))
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun fetchHistory(result: (Resource<List<Donation>>) -> Unit) {
        //val user = FirebaseAuth.getInstance().currentUser
        val userId = auth.currentUser!!.uid
        database.collection("donations").whereEqualTo("id", userId)
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

    override suspend fun udpateDonation(
        donation: Donation,
        data: HashMap<String,Any>,
        result: (Resource<List<Donation>>) -> Unit
    ) {
        database.collection("donation")
            .document(donation.donorId!!)
            .update(data)
            .addOnSuccessListener {
                result.invoke(
                    Resource.Success(listOf(donation))
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun getAllUsersTotalNumber(result: (Resource<Int>) -> Unit) {
        database.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                val users = ArrayList<User>()
                for (data in snapshot.documents) {
                    val user = data.toObject(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                result.invoke(
                    Resource.Success(users.size)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun getTotalDonations(result: (Resource<Int>) -> Unit) {
        database.collection("donations")
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
                    Resource.Success(donations.size)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun getTotalDonors(result: (Resource<Int>) -> Unit) {
        database.collection("users")
            .whereEqualTo("user_type", "Restaurant")
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
                    Resource.Success(donations.size)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }

    override suspend fun getAllDonations(result: (Resource<List<Donation>>) -> Unit) {
        database.collection("donations")
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

    override suspend fun getAllUsers(result: (Resource<List<User>>) -> Unit) {
        database.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                val users = ArrayList<User>()
                for (data in snapshot.documents) {
                    val user = data.toObject(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                result.invoke(
                    Resource.Success(users)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(it.message.toString())
                )
            }
    }
}