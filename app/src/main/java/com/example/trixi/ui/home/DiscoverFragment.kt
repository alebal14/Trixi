package com.example.trixi.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.entities.Post
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.fragments.EmptyHomeFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home.*


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        Log.d("home", "in home fragment")
        setUpDiscoverView()

        home_item_following.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, HomepageFragment())
                commit()
            }
        }

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


    private fun setUpDiscoverView() {

            model.getDiscoverPosts()?.observe(viewLifecycleOwner, Observer { posts ->
                populatePosts(posts)
            })

    }

    private fun populatePosts(posts: List<Post>?) {

        posts?.forEach { post ->
            model.getOneUser(post.ownerId!!)
                ?.observe(viewLifecycleOwner, Observer { postOwner ->
                    if (postOwner != null) {
                        post.owner = postOwner
                        adapter.add(HomeItem(post, fm!!))

                    } else {
                        Log.d("home", "user null")
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


