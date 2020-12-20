package com.example.trixi.ui.discover

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.RealmPost
import com.example.trixi.repository.DataViewModel
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_top_liked_posts.*

class ShowTopPostsFragment : Fragment() {
    //val model: DataViewModel by viewModels()
    private val postList : RealmResults<RealmPost>? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_liked_posts, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        getData()
        Log.d("discover", postList.toString())


        media_grid_top_posts.apply {
            media_grid_top_posts.layoutManager =
                StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
            adapter = DiscoverMediaGridAdapter(postList)
            media_grid_top_posts.adapter = adapter
        }

        addData()

    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.profile_nav_menu, menu)
//        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Discover")
//        super.onCreateOptionsMenu(menu, inflater)
//    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Discover")
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun addData() {
        //Picasso.get().load(RetrofitClient.BASE_URL).fit().into(image_top_post)
    }

    private fun getData(){
//        model.getAllPostsData()
//            .observe(viewLifecycleOwner) { postsA ->
//                Log.d("post", " all posts in db : ${postsA.size}")
//            }


    }

}