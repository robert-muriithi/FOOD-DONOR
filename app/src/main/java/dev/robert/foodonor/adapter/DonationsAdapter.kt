package dev.robert.foodonor.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.robert.foodonor.R
import dev.robert.foodonor.databinding.DonationsRowLayoutBinding
import dev.robert.foodonor.fragments.DonationsFragmentDirections
import dev.robert.foodonor.model.Donation

class DonationsAdapter : ListAdapter<Donation, DonationsAdapter.DonationsViewHolder>(COMPARATOR) {
    class DonationsViewHolder(private var binding: DonationsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(donation: Donation?) {
            binding.donatedFoodItem.text = donation?.foodItem
            binding.donatedFoodItemDescription.text = donation?.description
            binding.donorPhoneNumber.setOnClickListener {
                //start phone call
                val phoneNumber = donation?.phoneNumber
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                binding.root.context.startActivity(intent)
            }

            binding.donorLocation.setOnClickListener {

                //navigate to maps fragment
                val action = DonationsFragmentDirections.actionDonationsFragmentToDonorLocationFragment(
                    donation!!
                )
                binding.root.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DonationsViewHolder {
        return DonationsViewHolder(
            DonationsRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DonationsViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    object COMPARATOR : DiffUtil.ItemCallback<Donation>() {
        override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem == newItem
        }
    }
}