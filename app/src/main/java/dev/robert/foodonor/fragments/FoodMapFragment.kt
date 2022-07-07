package dev.robert.foodonor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.foodonor.R
import dev.robert.foodonor.databinding.FragmentFoodMapBinding

@AndroidEntryPoint
class FoodMapFragment : Fragment() {
   private lateinit var binding: FragmentFoodMapBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodMapBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }


}