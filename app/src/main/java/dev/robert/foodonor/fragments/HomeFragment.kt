package dev.robert.foodonor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.foodonor.R
import dev.robert.foodonor.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.show()
        auth = FirebaseAuth.getInstance()

        binding.cardLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        binding.cardDonate.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_donateFragment)
        }
        binding.cardReceive.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_receiveFragment)
        }
        binding.cardMyPin.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_donationsFragment)
        }
        binding.cardFoodmap.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_foodMapFragment)
        }
        binding.cardAboutus.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aboutUsFragment)
        }
        binding.cardContact.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_contactUsFragment)
        }

        return view
    }

}