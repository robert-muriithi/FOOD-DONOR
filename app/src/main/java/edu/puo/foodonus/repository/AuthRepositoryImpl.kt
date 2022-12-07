package edu.puo.foodonus.repository

import android.app.Application
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.puo.foodonus.model.User
import edu.puo.foodonus.utils.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String,
        result: (Resource<String>) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                if(currentUser!!.isEmailVerified){
                    db.collection("users")
                        .document(currentUser.uid)
                        .get()
                        .addOnSuccessListener {
                            val userType = it.get("user_type") as String

                            when (userType) {
                                "Admin" -> {
                                    result.invoke(Resource.Success("Admin"))
                                }
                                "Organization" -> {
                                    result.invoke(
                                        Resource.Success("Organization")
                                    )
                                }
                                else -> {
                                    result.invoke(Resource.Success("Restaurant"))
                                }
                            }
                        }
                }else{
                    result.invoke(Resource.Error("Email not verified"))
                }
            }.addOnFailureListener {
                result.invoke(Resource.Error(it.message.toString()))
            }

    }

    override suspend fun register(
        email: String,
        password: String,
        user: User,
        result: (Resource<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                auth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        db.collection("users")
                            .document(auth.uid.toString())
                            .set(user)
                            .addOnSuccessListener {
                                result.invoke(Resource.Success("Account Created Successfully\n Check your email for verification Link"))
                            }
                    }
            }
            .addOnFailureListener {
                result.invoke(Resource.Error(it.message.toString()))
            }
    }

    override suspend fun logout(result: () -> Unit) {
        auth.signOut()
        result.invoke()
    }
}