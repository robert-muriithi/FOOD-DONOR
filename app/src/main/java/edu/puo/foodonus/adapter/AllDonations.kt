package edu.puo.foodonus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.puo.foodonus.databinding.DonationItemBinding
import edu.puo.foodonus.model.Donation

class AllDonations : ListAdapter<Donation, AllDonations.DonationsViewHolder>(DonationDiffCallback) {
    object DonationDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Donation>() {
        override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationsViewHolder {
        return DonationsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DonationsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DonationsViewHolder private constructor(private val binding: DonationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Donation) {
            binding.foodItemName.text = item.name
            binding.description.text = item.foodItem
            binding.phoneNumber.text = item.phoneNumber
        }

        companion object {
            fun from(parent: ViewGroup): DonationsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DonationItemBinding.inflate(layoutInflater, parent, false)
                return DonationsViewHolder(binding)
            }
        }
    }
}
