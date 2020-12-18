package com.example.marvelisimo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.R
import com.example.trixi.entities.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_media_item.view.*


class ProfileMediaGridAdapter(
    private var posts: ArrayList<Post>)
    //private val listener:(Post) -> Unit)
    : RecyclerView.Adapter<ProfileMediaGridAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {



        val gridView = (LayoutInflater.from(parent.context).inflate(
            R.layout.media_grid,
            parent, false
        ))
        return ProfileViewHolder(gridView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bindView(posts[position]
            //,listener
    )
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ProfileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mediaItem: ImageView = itemView.media_item_thumbnail

        fun bindView(post: Post
                     //listener: (Post) -> Unit) {
        ){
            Picasso.get().load(post.filePath!!).into(mediaItem)
            //itemView.setOnClickListener { listener(post) }
        }

    }



//    class MediaItem() : Item<GroupieViewHolder>() {
//        //TODO make it post
//        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//            viewHolder.itemView.apply {
//                Picasso.get().load("https://imgur.com/IjMSpbA").into(media_item_thumbnail)
//            }
//        }
//        override fun getLayout() : Int = R.layout.profile_media_item
//    }
}