package dev.robert.foodonor.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.robert.foodonor.R
import dev.robert.foodonor.databinding.ReceiveRowLayoutBinding
import dev.robert.foodonor.fragments.ReceiveFragmentDirections
import dev.robert.foodonor.model.Donation


class ReceiveAdapter  : ListAdapter<Donation, ReceiveAdapter.ReceiveViewHolder>(ReceiveDiffUtil) {
    class ReceiveViewHolder(private val binding: ReceiveRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(donation: Donation?){

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
                val action = ReceiveFragmentDirections.actionReceiveFragmentToDonorLocationFragment(
                    donation!!
                )

                binding.root.findNavController().navigate(action)
            }

            binding.receiveCheckBox.setOnClickListener {

                val alertDialog = AlertDialog.Builder(binding.root.context)
                    .setTitle("Are you sure?")
                    .setMessage("Marking this as received means that you have already contacted the donor and organized on how to receive the donation or you have already received it. \n\nProceed?")
                    .setIcon(R.drawable.ic_warning)
                    .setCancelable(false)
                    .setPositiveButton("Yes"){ _, i ->
                        binding.receiveCheckBox.isChecked = true
                        binding.receiveCheckBox.isEnabled = false
                        binding.receiveCheckBox.text = "Received"
                        val donations = ArrayList<Donation>()
                        val donation = Donation(donation?.id, donation?.name, donation?.foodItem,donation?.phoneNumber,donation?.description, donation?.location, true)
                        if (donation.isReceived == true){
                            donations.add(donation)
                            binding.receiveCheckBox.isEnabled = false
                            binding.receiveCheckBox.isChecked = true
                        }
                    }
                    .setNegativeButton("No"){ _, i ->
                        binding.receiveCheckBox.isChecked = false
                        binding.receiveCheckBox.isEnabled = true

                    }.create()
                alertDialog.show()

            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceiveViewHolder {
        return ReceiveViewHolder(ReceiveRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ReceiveViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    object ReceiveDiffUtil : DiffUtil.ItemCallback<Donation>() {
        override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem == newItem
        }
    }
}