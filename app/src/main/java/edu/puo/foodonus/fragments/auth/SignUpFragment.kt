package edu.puo.foodonus.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import edu.puo.foodonus.R
import edu.puo.foodonus.activities.MainActivity
import edu.puo.foodonus.model.User
import edu.puo.foodonus.databinding.FragmentSignUpBinding
import edu.puo.foodonus.fragments.auth.viewmodel.SignUpViewModel
import edu.puo.foodonus.utils.CheckInternet
import edu.puo.foodonus.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    @Inject lateinit var auth: FirebaseAuth
    private  val viewModel : SignUpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as MainActivity).supportActionBar?.hide()

        /*firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Users")*/

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
                !(binding.btnResturant.isChecked || binding.btnOrganization.isChecked || binding.btnAdmin.isChecked) -> {
                    Toast.makeText(requireContext(), "Please select user type", Toast.LENGTH_SHORT).show()
                }

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


                    val selectedItemId = binding.radioGroup.checkedRadioButtonId
                    val selectedItem = binding.radioGroup.findViewById<RadioButton>(selectedItemId)
                    val userType = selectedItem.text.toString()

                    if (CheckInternet.isConnected(requireContext())) {
                        //Toast.makeText(activity, "Internet is available", Toast.LENGTH_SHORT).show()
                        binding.progressCircular.isVisible = true
                        val user = User(
                            email,
                            name,
                            phone,
                            userType
                        )
                        viewModel.register(email, password, user)
                        viewModel.registerRequest.observe(viewLifecycleOwner){
                            when(it){
                                is Resource.Loading -> {
                                    binding.progressCircular.isVisible = true
                                }
                                is Resource.Error -> {
                                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                                }
                                is Resource.Success -> {
                                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                                    requireActivity().onBackPressedDispatcher.onBackPressed()
                                }
                            }
                        }

                        /*auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val currentUser = auth.currentUser
                                    binding.progressCircular.isVisible = false
                                    currentUser?.sendEmailVerification()
                                        ?.addOnCompleteListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Verification link has been sent to you email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    val userId = currentUser?.uid
                                    val user =
                                        userId?.let { id -> User(email, name, phone, id) }
                                    if (userId != null) {
                                        databaseReference.child(userId).setValue(user)
                                        Toast.makeText(
                                            requireContext(),
                                            "Account created successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    requireActivity().onBackPressed()
                                }
                            }
                            .addOnFailureListener {
                                binding.progressCircular.isVisible = false
                                binding.btnRegister.isEnabled = true
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                    .show()
                            }*/
                    }
                }
            }
        }

        return view
    }

}