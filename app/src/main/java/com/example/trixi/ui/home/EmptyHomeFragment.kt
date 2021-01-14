package com.example.trixi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trixi.R
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import kotlinx.android.synthetic.main.fragment_empty_home.*


class EmptyHomeFragment : Fragment() {

    private var loggedInUser: User? = PostToDb.loggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_empty_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val msg = arguments?.getString("message")

        if (msg == "NoActivity") {
            empty_container.visibility = View.GONE;
            empty_text.text = "You have no activity"
        }

        home_item_discover.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, DiscoverFragment())
                commit()
            }
        }
    }

}