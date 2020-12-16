package com.example.marvelisimo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_media_item.view.*

class ProfileMediaGridAdapter(private var posts: ArrayList<Post>)
//private val listener:(Post) -> Unit)
    : RecyclerView.Adapter<ProfileMediaGridAdapter.ProfileMediaGridViewHolder>() {

    override fun getItemCount() = posts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMediaGridViewHolder {
        val gridView = (LayoutInflater.from(parent.context).inflate(
            R.layout.profile_media_item,
            parent, false
        ))
        return ProfileMediaGridViewHolder(gridView)
    }

    override fun onBindViewHolder(holder: ProfileMediaGridViewHolder, position: Int) {
        holder.bindThumbnail(
            posts[position]
            //,listener
        )
    }

    class ProfileMediaGridViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val mediaItem = itemView.profile_media_thumbnail

        init{
            view.setOnClickListener(this)
        }

        override fun onClick(view: View){
            Log.d("Recyclerview media grid", "click!")
        }
        fun bindThumbnail(post: Post) {
            Picasso.get().load(BASE_URL + post.filePath!!).into(mediaItem)
        }

    }

}