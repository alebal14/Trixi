package com.example.trixi.ui.discover

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.RealmPost
import com.squareup.picasso.Picasso
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_top_liked_post_item.view.*


class DiscoverMediaGridAdapter(private var posts: RealmResults<RealmPost>?)
    : RecyclerView.Adapter<DiscoverMediaGridAdapter.DiscoverViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {
        val gridView = (LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_top_liked_posts,
            parent, false
        ))
        return DiscoverViewHolder(gridView)
    }


    override fun getItemCount(): Int {
        if (posts == null){
            return 0;
        } else
        return  posts?.size!!
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        posts?.get(position)?.let { holder.bindView(it) }
    }




    class DiscoverViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val postItem = itemView.image_top_post

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            Log.d("click", "click")
        }

        fun bindView(post: RealmPost) {
            Picasso.get().load(BASE_URL + post.filePath!!).into(postItem)
            Log.d("bindView", "in bindview")
        }



    }




}