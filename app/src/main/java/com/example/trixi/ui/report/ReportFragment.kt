package com.example.trixi.ui.report

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.Activity
import com.example.trixi.entities.Post
import com.example.trixi.entities.Report
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.activity.ActivityAdapter
import com.example.trixi.ui.home.HomeAdapter
import com.example.trixi.ui.post.SinglePostFragment
import kotlinx.android.synthetic.main.fragment_activity.*


class ReportFragment : Fragment() {
    private var model: TrixiViewModel = TrixiViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        setUpReportView()
    }

    private fun setUpReportView() {
//

        model.getAllReports().observe(viewLifecycleOwner,{ reports ->
            recyclerView_activity.apply {
                val layoutManager = LinearLayoutManager(context)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView_activity.layoutManager = layoutManager
                adapter = ReportAdapter(
                    reports as ArrayList<Report>,
                    activity?.supportFragmentManager!!,
                    viewLifecycleOwner
                ){ report ->
                    redirectToSinglePost(report.post , report)
                }
            }


        })
    }

    private fun redirectToSinglePost(post: Post, report: Report) {
        val singlePost = SinglePostFragment(post, report)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, singlePost)?.addToBackStack("singelPostFragment")!!.commit()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Report"
        super.onCreateOptionsMenu(menu, inflater)
    }


}