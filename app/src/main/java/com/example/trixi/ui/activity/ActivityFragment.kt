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
import com.example.trixi.ui.home.EmptyHomeFragment
import com.example.trixi.ui.post.SinglePostFragment
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
        model.getActivityByOwner(PostToDb.loggedInUser?.uid.toString()).observe(viewLifecycleOwner,
            { activities ->
                if (!activities.isNullOrEmpty()) {
                    recyclerView_activity.apply {
                        val layoutManager = LinearLayoutManager(context)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        recyclerView_activity.layoutManager = layoutManager
                        adapter = ActivityAdapter(
                            activities as ArrayList<Activity>,
                            activity?.supportFragmentManager!!,
                            viewLifecycleOwner
                        ) { activity ->
                            redirectToSinglePost(activity.post)

                        }
                    }
                } else {
                    val bundle = Bundle()
                    bundle.putString("message", "NoActivity")
                    val emptyHomeFragment = EmptyHomeFragment()
                    emptyHomeFragment.arguments = bundle
                    activity?.supportFragmentManager?.beginTransaction()?.apply {
                        replace(
                            R.id.fragment_container,
                            emptyHomeFragment
                        ).addToBackStack("populateFragment")!!.commit()
                    }

                }
            })
    }

    private fun redirectToSinglePost(post: Post) {
        val singlePost = SinglePostFragment(post)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, singlePost)?.addToBackStack("singelPostFragment")!!.commit()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Activity"
        super.onCreateOptionsMenu(menu, inflater)
    }


}