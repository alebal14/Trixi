package com.example.trixi.ui.activity

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.Post
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_home.view.*


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
        model.getPostsByOwner(PostToDb.loggedInUser?.uid.toString())?.observe(viewLifecycleOwner,{ posts->
            if(posts.isNotEmpty()){
                recyclerView_activity.apply {
                    val layoutManager = LinearLayoutManager(context)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView_homepage.layoutManager = layoutManager
                    adapter = ActivityAdapter(
                        posts as ArrayList<Post>,
                        activity?.supportFragmentManager!!,
                        viewLifecycleOwner
                    )
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Activity"
        super.onCreateOptionsMenu(menu, inflater)
    }


}