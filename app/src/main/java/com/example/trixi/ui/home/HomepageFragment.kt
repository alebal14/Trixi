package com.example.trixi.ui.home

//import com.example.trixi.apiService.RetrofitClient

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.trixi.R
import com.example.trixi.repository.DataViewModel
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.EmptyHomeFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home_item.*
import kotlinx.android.synthetic.main.fragment_home_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class HomepageFragment : Fragment() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    val model: DataViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)


//        PostToDb.loggedInUser?.uid?.let {
//            model.getFollowingsPostsData(it)
//                .observe(viewLifecycleOwner) { postsF ->
//                    Log.d("post", "following all posts : ${postsF.size}")
//                }
//        }

        model.getAllPostsData()
            .observe(viewLifecycleOwner) { postsA ->
                Log.d("post", " all posts : ${postsA.size}")
            }

        setupRecycleView()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Trixi")
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun setupRecycleView() {


        val adapter = GroupAdapter<GroupieViewHolder>()
        val fm = fragmentManager
        adapter.clear()
        PostToDb.loggedInUser!!.uid?.let {
            model.getFollowingsPostFromDb(it).observe(viewLifecycleOwner) { posts ->
                if (posts.isEmpty()) {
                    activity?.supportFragmentManager?.beginTransaction()?.apply {
                        replace(R.id.fragment_container, EmptyHomeFragment())
                        commit()
                    }
                } else {
                    Log.d("post", "followings posts : ${posts.size}")
                    posts.forEach { post ->
                        model.getOneUserFromDb(post.ownerId)
                            .observe(viewLifecycleOwner) { postOwner ->
                                adapter.add(HomeItem(post, postOwner, fm!!))
                            }
                    }
                    recyclerView_homepage.adapter = adapter
                }

            }
        }


        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView_homepage);


    }


}