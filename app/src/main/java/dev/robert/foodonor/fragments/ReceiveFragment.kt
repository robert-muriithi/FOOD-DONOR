package dev.robert.foodonor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.foodonor.R
import dev.robert.foodonor.adapter.ReceiveAdapter
import dev.robert.foodonor.databinding.FragmentReceiveBinding
import dev.robert.foodonor.utils.Resource
import dev.robert.foodonor.viewmodel.DonationsViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReceiveFragment : Fragment() {
    private lateinit var binding: FragmentReceiveBinding
    private val adapter : ReceiveAdapter by lazy { ReceiveAdapter() }
    private val viewModel: DonationsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReceiveBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.receiveRv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getDonations()
        }

        viewModel.donations.observe(viewLifecycleOwner){ state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    adapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }



        return view
    }

}