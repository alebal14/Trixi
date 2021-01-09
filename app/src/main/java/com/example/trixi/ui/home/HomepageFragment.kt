package com.example.trixi.ui.home

//import com.example.trixi.apiService.RetrofitClient

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.entities.Comment
import com.example.trixi.entities.Like
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.fragments.EmptyHomeFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home.*


class HomepageFragment : Fragment() {

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
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        Log.d("home", "in home fragment")
        setUpHomeView()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Trixi"
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun setUpHomeView() {
        PostToDb.loggedInUser?.uid?.let {
            model.getFollowingsPosts(it)?.observe(viewLifecycleOwner, Observer { posts ->
                Log.d("home", "Followings post size: ${posts?.size}")
                populatePosts(posts)
            })
        }
    }


    private fun populatePosts(posts: List<Post>?) {
        if (posts!!.isEmpty()) {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, EmptyHomeFragment()).addToBackStack("populateFragment")!!.
                commit()
            }
        } else {
            //posts.forEachIndexed { index, post ->
            for (post in posts) {

                Log.d("home", "post title: ---${post.title}")
                model.getOneUser(post.ownerId!!)
                    ?.observe(viewLifecycleOwner, Observer { postOwner ->
                        if (postOwner != null) {
                            post.owner = postOwner

                            adapter.add(HomeItem(post, fm!!))


                        } else {
                            // Log.d("home", "user null")
                            model.getOnePet(post.ownerId!!)
                                ?.observe(viewLifecycleOwner, Observer { petIsOwner ->
                                    post.ownerIsPet = petIsOwner
                                    adapter.add(HomeItem(post, fm!!))
                                })
                        }
                    })

            }


            recyclerView_homepage.adapter = adapter

        }
    }
}