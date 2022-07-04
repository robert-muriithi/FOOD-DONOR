package dev.robert.foodonor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.robert.foodonor.R
import dev.robert.foodonor.databinding.FragmentDonateBinding

class DonateFragment : Fragment() {
    private lateinit var binding: FragmentDonateBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDonateBinding.inflate(inflater, container, false)
        val view = binding.root





        return view
    }

}