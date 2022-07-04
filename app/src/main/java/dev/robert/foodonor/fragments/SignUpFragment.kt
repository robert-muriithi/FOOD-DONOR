package dev.robert.foodonor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.robert.foodonor.R
import dev.robert.foodonor.activities.MainActivity
import dev.robert.foodonor.data.User
import dev.robert.foodonor.databinding.FragmentSignUpBinding
import dev.robert.foodonor.utils.CheckInternet

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase : FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as MainActivity).supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Users")

        binding.loginTv.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            val name = binding.nameInputLayout.editText?.text.toString().trim()
            val email = binding.emailInputLayout.editText?.text.toString().trim()
            val phone = binding.phoneNumberInputLayout.editText?.text.toString().trim()
            val password = binding.passInputLayout.editText?.text.toString().trim()
            val confirmPassword = binding.confPassInputLayout.editText?.text.toString().trim()

            when {
                name.isEmpty() -> {
                    binding.nameInputLayout.error = "Name is required"
                    binding.nameInputLayout.isErrorEnabled = true
                }
                email.isEmpty() -> {
                    binding.emailInputLayout.error = "Email is required"
                    binding.emailInputLayout.isErrorEnabled = true
                }
                phone.isEmpty() -> {
                    binding.phoneNumberInputLayout.error = "Phone number is required"
                    binding.phoneNumberInputLayout.isErrorEnabled = true
                }
                password.isEmpty() -> {
                    binding.passInputLayout.error = "Password is required"
                    binding.passInputLayout.isErrorEnabled = true
                }
                confirmPassword.isEmpty() -> {
                    binding.confPassInputLayout.error = "Confirm password is required"
                    binding.confPassInputLayout.isErrorEnabled = true
                }
                password != confirmPassword -> {
                    binding.passInputLayout.error = "Password does not match"
                    binding.confPassInputLayout.error = "Password does not match"
                    binding.passInputLayout.isErrorEnabled = true
                }
                else -> {
                    binding.nameInputLayout.isErrorEnabled = false
                    binding.emailInputLayout.isErrorEnabled = false
                    binding.phoneNumberInputLayout.isErrorEnabled = false
                    binding.passInputLayout.isErrorEnabled = false
                    binding.confPassInputLayout.isErrorEnabled = false
                    binding.btnRegister.isEnabled = false

                    Toast.makeText(requireContext(), "Registering", Toast.LENGTH_SHORT).show()

                    if (CheckInternet.isConnected(requireContext())){
                        //Toast.makeText(activity, "Internet is available", Toast.LENGTH_SHORT).show()
                        binding.progressCircular.isVisible = true
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful){
                                    val currentUser = auth.currentUser
                                    binding.progressCircular.isVisible = false
                                    currentUser?.sendEmailVerification()?.addOnCompleteListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Verification link has been sent to you email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    val userId = currentUser?.uid
                                    val user =
                                        userId?.let { id -> User(email, name, phone,  id) }
                                    if (userId != null) {
                                        databaseReference.child(userId).setValue(user)
                                        Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            .addOnFailureListener {
                                binding.progressCircular.isVisible = false
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }

        return view
    }

}