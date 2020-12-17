package com.example.trixi.ui.discover

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_top_liked_post_item.view.*


class DiscoverMediaGridAdapter
    (private var posts: ArrayList<Post>) : RecyclerView.Adapter<DiscoverMediaGridAdapter.DiscoverViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {
        val gridView : View = (LayoutInflater.from(parent.context).inflate(R.layout.fragment_top_liked_posts, parent, false))
        return DiscoverViewHolder(gridView)
    }


    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        holder.bindView(posts[position]);
    }


    class DiscoverViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val postItem = itemView.image_top_post

        init {
            view.setOnClickListener(this)
        }

        fun bindView(post: Post) {
            Picasso.get().load(BASE_URL + post.filePath!!).into(postItem)
        }

        override fun onClick(p0: View?) {
            Log.d("click", "click")
        }

    }



}