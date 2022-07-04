package dev.robert.foodonor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.robert.foodonor.R
import dev.robert.foodonor.databinding.FragmentReceiveBinding

class ReceiveFragment : Fragment() {
    private lateinit var binding: FragmentReceiveBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReceiveBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

}