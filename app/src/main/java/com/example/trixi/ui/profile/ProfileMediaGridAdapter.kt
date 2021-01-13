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

class ProfileMediaGridAdapter(private var posts: ArrayList<Post>,
                              private val listener: (Post) ->Unit)
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
            posts[position],
            listener
        )
    }

     class ProfileMediaGridViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val image = itemView.profile_image_thumbnail
         val video = itemView.profile_video_thumbnail

        override fun onClick(view: View) {
            Log.d("Recyclerview media grid", "click!")
        }

        fun bindThumbnail(post: Post, listener: (Post) -> Unit) {

            if (post.fileType.toString() == "image") {
                image.visibility = View.VISIBLE
                video.visibility = View.GONE
                val photo = BASE_URL + post.filePath
                Picasso.get().load(photo).placeholder(R.drawable.sample).error(R.drawable.sample)
                    .centerCrop().fit().into(image)

            } else {
               image.visibility = View.GONE
                video.visibility = View.VISIBLE
                video.setSource(BASE_URL + post.filePath.toString())
            }
            itemView.setOnClickListener { listener(post)}
        }
    }

}