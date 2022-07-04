package dev.robert.foodonor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import dev.robert.foodonor.R
import dev.robert.foodonor.activities.MainActivity
import dev.robert.foodonor.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
   private lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as MainActivity).supportActionBar?.hide()

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        binding.signupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
        }

        return view
    }

}