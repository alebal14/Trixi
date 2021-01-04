package com.example.trixi.ui.discover

//import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.Post
import com.example.trixi.repository.TrixiViewModel
import kotlinx.android.synthetic.main.fragment_top_liked_posts.*

class ShowTopPostsFragment : Fragment() {
    private lateinit var model: TrixiViewModel
    //private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_liked_posts, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        super.onViewCreated(view, savedInstanceState)
        setupDiscoverFragment()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Discover"
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun setupDiscoverFragment() {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        model.getAllPosts()?.observe(viewLifecycleOwner, Observer { post ->
            if (!post.isNullOrEmpty()){
            media_grid_top_posts.apply {
                media_grid_top_posts.layoutManager =
                    StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                media_grid_top_posts.adapter = DiscoverMediaGridAdapter(post as ArrayList<Post>)
                //media_grid_top_posts.adapter = adapter
            }

        }})


    }
}