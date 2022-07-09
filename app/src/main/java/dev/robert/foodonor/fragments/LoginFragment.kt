package dev.robert.foodonor.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.foodonor.R
import dev.robert.foodonor.model.User
import dev.robert.foodonor.databinding.FragmentLoginBinding
import dev.robert.foodonor.utils.CheckInternet

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root


        (activity as AppCompatActivity).supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.registerTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.forgotPasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailTinputLayout.editText?.text.toString()
            val password = binding.passwordInputLayout.editText?.text.toString()

            when {
                email.isEmpty() -> {
                    binding.emailTinputLayout.error = "Email is required"
                    binding.emailTinputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.passwordInputLayout.error = "Password is required"
                    binding.passwordInputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailTinputLayout.error = "Invalid email format"
                    binding.emailTinputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                else -> {
                    binding.emailTinputLayout.isErrorEnabled = false
                    binding.passwordInputLayout.isErrorEnabled = false
                    binding.btnLogin.isEnabled = false

                    if (CheckInternet.isConnected(requireActivity())) {
                        //Toast.makeText(activity, "Internet is available", Toast.LENGTH_SHORT).show()
                        binding.emailTinputLayout.isEnabled = false
                        binding.passwordInputLayout.isEnabled = false
                        binding.progressCircular.isVisible = true
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity()) { task ->
                                val currentUser = auth.currentUser
                                if (task.isSuccessful) {
                                    if (currentUser!!.isEmailVerified) {
                                        databaseReference =
                                            FirebaseDatabase.getInstance().getReference("Users")
                                        val firebaseUser: FirebaseUser? =
                                            FirebaseAuth.getInstance().currentUser
                                        val uid: String? = firebaseUser?.uid
                                        uid?.let { id ->
                                            databaseReference.child(id)
                                                .addValueEventListener(object : ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        val user =
                                                            snapshot.getValue(User::class.java)
                                                        if (user != null) {
                                                            binding.progressCircular.isVisible =
                                                                false
                                                            Toast.makeText(
                                                                requireContext(),
                                                                "Login in successful",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                                                        }
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        binding.progressCircular.isVisible = false
                                                        binding.btnLogin.isEnabled = true
                                                        binding.emailTinputLayout.editText?.text?.clear()
                                                        binding.passwordInputLayout.editText?.text?.clear()
                                                        binding.emailTinputLayout.isEnabled = true
                                                        binding.passwordInputLayout.isEnabled = true
                                                        Toast.makeText(
                                                            activity,
                                                            "Error: ${error.message}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                })
                                        }

                                    } else {
                                        binding.progressCircular.isVisible = false
                                        Toast.makeText(
                                            requireContext(),
                                            "Please verify your email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.emailTinputLayout.editText?.text?.clear()
                                        binding.passwordInputLayout.editText?.text?.clear()
                                        binding.emailTinputLayout.isEnabled = true
                                        binding.passwordInputLayout.isEnabled = true
                                    }
                                } else {
                                    binding.progressCircular.isVisible = false
                                    binding.btnLogin.isEnabled = true
                                    binding.emailTinputLayout.editText?.text?.clear()
                                    binding.passwordInputLayout.editText?.text?.clear()
                                    binding.emailTinputLayout.isEnabled = true
                                    binding.passwordInputLayout.isEnabled = true
                                    Toast.makeText(
                                        activity,
                                        "Error: ${task.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                    } else {
                        Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT)
                            .show()
                        binding.progressCircular.isVisible = false
                    }
                }
            }
        }
        return view
    }

}