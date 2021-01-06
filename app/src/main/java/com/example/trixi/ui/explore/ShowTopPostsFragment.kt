package com.example.trixi.ui.explore

//import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.marvelisimo.adapter.ProfileMediaGridAdapter
import com.example.trixi.R
import com.example.trixi.entities.Post
import com.example.trixi.repository.TrixiViewModel
import kotlinx.android.synthetic.main.fragment_profile.view.*
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

        search_bar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                search_bar.setIconified(false)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Discover"
        super.onCreateOptionsMenu(menu, inflater)
    }




    private fun setupDiscoverFragment() {
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
      
        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {


                model.getAllPosts().observe(viewLifecycleOwner, Observer { post ->

                    if (newText != null) {
                        val finalList =
                            post!!.filter {
                                it.title!!.startsWith(newText!!) || it.description!!.startsWith(
                                    newText!!
                                ) || it.categoryName!!.startsWith(newText!!)
                            }.map { it!! }
                        println("FINALS " + finalList.size)
                        for (p in finalList) {
                            println("FINALP " + p)
                        }
                         media_grid_top_posts.apply {
                   media_grid_top_posts.layoutManager =
                       StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                   StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                   media_grid_top_posts.adapter = ExploreMediaGridAdapter(finalList as ArrayList<Post>)
               }
                    }
                    else{
                         media_grid_top_posts.apply {
                   media_grid_top_posts.layoutManager =
                       StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                   StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                   media_grid_top_posts.adapter = ExploreMediaGridAdapter(post as ArrayList<Post>)
               }
                    }
                })



                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

        })


     /*   model.getAllPosts()?.observe(viewLifecycleOwner, Observer { post ->
            Log.d("post_size_f", post?.size.toString())

            media_grid_top_posts.apply {
                media_grid_top_posts.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                media_grid_top_posts.adapter = ExploreMediaGridAdapter(post as ArrayList<Post>)
            }

        })*/


    }
}