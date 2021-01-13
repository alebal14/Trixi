package com.example.trixi.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.Post
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recyclerView_homepage
import kotlinx.android.synthetic.main.fragment_home.view.*


class DiscoverFragment : Fragment() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var model: TrixiViewModel
    private val fm: FragmentManager?
        get() {
            return fragmentManager
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        Log.d("home", "in home fragment")
        setUpDiscoverView()
        pullToRefresh.setOnRefreshListener {
            Log.d("home", "pull to refresh called")
            setUpDiscoverView()
            if (pullToRefresh.isRefreshing) {
                pullToRefresh.isRefreshing = false;
            }
        }

        refresh_button.setOnClickListener {
            Log.d("home", "in home fragment")
            val deg = refresh_button.rotation + 180F
            refresh_button.animate().rotation(deg).interpolator = AccelerateDecelerateInterpolator()
            setUpDiscoverView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Discover"
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun setUpDiscoverView() {
        PostToDb.loggedInUser?.uid?.let {
            model.getDiscoverPosts(it)?.observe(viewLifecycleOwner, Observer { posts ->
                Log.d("home", "Discover post size: ${posts?.size}")
                populatePosts(posts)
            })
        }
    }

    private fun populatePosts(posts: List<Post>?) {
        recyclerView_homepage.apply {
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView_homepage.layoutManager = layoutManager
            adapter = HomeAdapter(
                posts as ArrayList<Post>,
                activity?.supportFragmentManager!!,
                viewLifecycleOwner,
                "discover"
            )
        }
    }

}




