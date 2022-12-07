package edu.puo.foodonus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.puo.foodonus.databinding.UserItemBinding
import edu.puo.foodonus.model.User

class AllUsers : ListAdapter<User, AllUsers.UsersViewHolderl>(UsersDiffUtil) {
    class UsersViewHolderl (private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User) {
            binding.userName.text = item.name
            binding.phoneNumber.text = item.phone
        }
    }


    object UsersDiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.phone == newItem.phone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolderl {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolderl(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolderl, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}