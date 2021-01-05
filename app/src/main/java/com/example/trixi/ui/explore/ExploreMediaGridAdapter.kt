package com.example.trixi.ui.explore

//import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_top_liked_post_item.view.*


class ExploreMediaGridAdapter(private val posts: ArrayList<Post>)
    : RecyclerView.Adapter<ExploreMediaGridAdapter.DiscoverViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {
        val gridView = (LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_top_liked_post_item,
            parent, false
        ))
        return DiscoverViewHolder(gridView)
    }


    override fun getItemCount(): Int {
        Log.d("posts_size", posts.size.toString())
        return posts.size
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        posts?.get(position)?.let { holder.bindView(it) }
    }


    class DiscoverViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val postItem: ImageView? = itemView.image_top_post

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            Log.d("click", "clicking on image")
        }

        fun bindView(post: Post) {
            var photo: String = BASE_URL + post.filePath
            Picasso.get().load(photo).into(postItem)
        }



    }




}