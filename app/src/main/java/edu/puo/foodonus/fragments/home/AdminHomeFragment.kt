package edu.puo.foodonus.fragments.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import edu.puo.foodonus.R
import edu.puo.foodonus.adapter.AllDonations
import edu.puo.foodonus.adapter.AllUsers
import edu.puo.foodonus.databinding.FragmentAdminHomeBinding
import edu.puo.foodonus.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class AdminHomeFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentAdminHomeBinding
    private val viewModel by viewModels<AdminHomeViewModel>()
    private val instance = this
    private val usersAdapter by lazy { AllUsers(instance) }
    private val donationsAdapter by lazy { AllDonations(instance) }
    @Inject lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().addMenuProvider(this, viewLifecycleOwner )
        //(activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.show()
        binding.allUsers.adapter = usersAdapter
        binding.allDonations.adapter = donationsAdapter
        fetchData()

        return view
    }

     fun fetchData() {
        viewModel.getAllUsers()
        viewModel.getAllDonations()
        viewModel.getAllUsersTotalNumber()
        viewModel.getTotalDonations()
        viewModel.getAllDonorsTotalNumber()

        viewModel.totalDonors.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    binding.totalDonors.text = it.data.toString()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {

                }
            }
        }

        viewModel.totalDonations.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    binding.totalDonations.text = it.data.toString()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {

                }
            }
        }

        viewModel.totalUsers.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    binding.totalUsers.text = it.data.toString()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {

                }
            }
        }

        viewModel.allDonations.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    donationsAdapter.submitList(it.data)
                    binding.progressBarRV2.visibility = View.GONE
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                    binding.progressBarRV2.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.progressBarRV2.visibility = View.VISIBLE
                }
            }
        }
        viewModel.users.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    usersAdapter.submitList(it.data)
                    binding.progressBarRV.visibility = View.GONE
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                    binding.progressBarRV.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.progressBarRV.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.admin_menu, menu)
        menu.findItem(R.id.action_logout).setOnMenuItemClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_adminHomeFragment_to_loginFragment)
            true
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.action_logout -> {
                auth.signOut()
                findNavController().navigate(R.id.action_adminHomeFragment_to_loginFragment)
            }
        }
        return true
    }

}