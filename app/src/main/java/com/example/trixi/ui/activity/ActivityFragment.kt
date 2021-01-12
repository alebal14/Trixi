package com.example.trixi.ui.activity

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.Activity
import com.example.trixi.entities.Post
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.home.HomeAdapter
import kotlinx.android.synthetic.main.fragment_activity.*


class ActivityFragment : Fragment() {
    private var model: TrixiViewModel = TrixiViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        setUpActivityView()
    }

    private fun setUpActivityView() {
//        model.getPostsByOwner(PostToDb.loggedInUser?.uid.toString())
//            ?.observe(viewLifecycleOwner, { posts ->
//                if (posts.isNotEmpty()) {
//                    Log.d("PageAct", "post size on activity ${posts.size}")
//                    posts.forEach { post ->
//
//                        recyclerView_activity.apply {
//                            val layoutManager = LinearLayoutManager(context)
//                            layoutManager.orientation = LinearLayoutManager.VERTICAL
//                            recyclerView_activity.layoutManager = layoutManager
//                            if (post.likes!!.isNotEmpty()) {
//                                adapter = ActivityAdapter(
//                                    post.likes as ArrayList<Like>,
//                                    null,
//                                    post,
//                                    activity?.supportFragmentManager!!,
//                                    viewLifecycleOwner
//                                ) {
//                                    redirectToSinglePost(it)
//                                }
//                            }
//
//                            if (post.comments!!.isNotEmpty()) {
//                                adapter = ActivityAdapter(
//                                    null,
//                                    post.comments as ArrayList<Comment>,
//                                    post,
//                                    activity?.supportFragmentManager!!,
//                                    viewLifecycleOwner
//                                ) {
//                                    redirectToSinglePost(it)
//                                }
//                            }
//                        }
//
//
//
//                    }
//
//                }
//
//            })

        model.getActivityByOwner(PostToDb.loggedInUser?.uid.toString()).observe(viewLifecycleOwner,{ activities->
            recyclerView_activity.apply {
                val layoutManager = LinearLayoutManager(context)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView_activity.layoutManager = layoutManager
                adapter = ActivityAdapter(
                    activities as ArrayList<Activity>,
                    activity?.supportFragmentManager!!,
                    viewLifecycleOwner
                ){

                }
            }


        })
    }

    private fun redirectToSinglePost(post: Post) {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Activity"
        super.onCreateOptionsMenu(menu, inflater)
    }


}