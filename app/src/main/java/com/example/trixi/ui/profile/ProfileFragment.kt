package com.example.trixi.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.Post
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()
        profile_media_grid.adapter = adapter

        adapter.apply {
            val gridLayoutManager = GridLayoutManager(
                context,
                2,
                GridLayoutManager.VERTICAL,
                false
            )
            val adapter = GroupAdapter<GroupieViewHolder>().apply {
                val gridSection = Section()
                add(gridSection)
                //setOnItemClickListener(onItemClick)
            }

        }


}

    class MediaItem(val post: Post) : Item() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            //viewHolder.itemView.background = post.path
        }

        override fun getLayout() = R.layout.profile_media_item

    }


}