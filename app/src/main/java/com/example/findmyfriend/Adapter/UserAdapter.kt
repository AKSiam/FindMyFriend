package com.example.findmyfriend.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmyfriend.Model.User
import com.example.findmyfriend.databinding.ItemUserBinding

class UserAdapter(private var userList: List<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(private val binding: ItemUserBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(user:User){
            binding.apply {
                binding.displayNameTxt.text = user.displayName
                binding.emailTxt.text = user.email
                binding.locationTxt.text = user.location
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.bind(user)

    }
    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }
}