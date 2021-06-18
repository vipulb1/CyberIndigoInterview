package com.me.cyberindigointerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.me.cyberindigointerview.R
import com.me.cyberindigointerview.model.UsersData

class Usersadapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var userList = ArrayList<UsersData.UserInfo?>()

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_user, parent, false)
            UserViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.dialog_progress, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) {
            if (userList[position] != null) {
                holder.bindItems(userList[position]!!)
            }
        } else if (holder is LoadingViewHolder) {
            holder.showLoadingView()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (userList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun setData(newUserList: ArrayList<UsersData.UserInfo?>?) {
        if (newUserList != null) {
            if (userList.isNotEmpty())
                userList.removeAt(userList.size - 1)
            userList.clear()
            userList.addAll(newUserList)
        } else {
            userList.add(newUserList)
        }
        notifyDataSetChanged()
    }

    fun getData() = userList

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userImage: ImageView = itemView.findViewById(R.id.user_images)
        private val userName: TextView = itemView.findViewById(R.id.user_names)
        private val userEmail: TextView = itemView.findViewById(R.id.user_emails)

        @SuppressLint("SetTextI18n")
        fun bindItems(userData: UsersData.UserInfo) {
            userName.text = userData.first_name
            userEmail.text = userData.email
            Glide.with(userImage.context).load(userData.avatar)
                .circleCrop()
                .centerCrop()
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userImage)
        }

    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iv_logo: ImageView = itemView.findViewById(R.id.iv_logo)

        fun showLoadingView() {
            val rotate = RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            rotate.duration = 1000
            rotate.repeatCount = Animation.INFINITE
            iv_logo.startAnimation(rotate)
        }
    }
}