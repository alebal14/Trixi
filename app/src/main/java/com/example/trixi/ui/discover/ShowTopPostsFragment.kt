package com.example.trixi.ui.discover

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trixi.R
import kotlinx.android.synthetic.main.fragment_top_liked_posts.*

class ShowTopPostsFragment : Fragment() {

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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_nav_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Discover")
        super.onCreateOptionsMenu(menu, inflater)
    }

}