package com.example.trixi.ui.discover

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_top_liked_post_item.*
import kotlinx.android.synthetic.main.fragment_top_liked_posts.*

class ShowTopPostsFragment : Fragment()  {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_top_liked_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        media_grid_top_posts.layoutManager = GridLayoutManager(context,2)
        //media_grid_top_posts.adapter = DiscoverMediaGridAdapter(posts)
        addData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_nav_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Discover"
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun addData() {
        Picasso.get().load(RetrofitClient.BASE_URL).fit().into(image_top_post)
    }

}