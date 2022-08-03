package dev.robert.foodonor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.robert.foodonor.databinding.ReceiveRowLayoutBinding
import dev.robert.foodonor.model.Donation

class ReceiveAdapter : ListAdapter<Donation, ReceiveAdapter.ReceiveViewHolder>(ReceiveDiffUtil) {
    object ReceiveDiffUtil : DiffUtil.ItemCallback<Donation>() {
        override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem == newItem
        }
    }

    class ReceiveViewHolder(private val binding: ReceiveRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(donation: Donation){

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceiveViewHolder {
        return ReceiveViewHolder(ReceiveRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ReceiveAdapter.ReceiveViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}