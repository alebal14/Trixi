package com.example.trixi.ui.discover

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelisimo.adapter.ProfileMediaGridAdapter
import com.example.trixi.entities.Post


class DiscoverMediaGridAdapter (
    private var posts: ArrayList<Post>)
    : RecyclerView.Adapter<DiscoverMediaGridAdapter.DiscoverViewHolder>(){




    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        holder.bindView(posts[position]);
    }

    fun setData(){

    }


    class DiscoverViewHolder (view: View) : RecyclerView.ViewHolder(view) {


        fun bindView(post: Post
            //listener: (Post) -> Unit) {
        ){


        }

    }

}