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
                              private val listener: OnItemClickListener
)
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

    inner class ProfileMediaGridViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val mediaItem = itemView.profile_media_thumbnail

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position:Int= adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }

            Log.d("Recyclerview media grid", "click!")
        }

        fun bindThumbnail(post: Post) {
            var photo = BASE_URL + post.filePath
            //var sample = BASE_URL + "resFolder/images/sample.jpg"


            Picasso.get().load(photo).placeholder(R.drawable.sample).error(R.drawable.sample)
                .resize(100, 100).centerCrop().into(mediaItem)


        }

    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

}