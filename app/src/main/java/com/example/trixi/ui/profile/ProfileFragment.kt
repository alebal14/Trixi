package com.example.trixi.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trixi.R
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    val user: User? = PostToDb.loggedInUser


    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleView(view)
        setUpProfile()
    }

    private fun setUpProfile() {

        if(user != null) {
            profile_name.text = user.userName
            profile_bio.text = user.bio
            //TODO load image from user
            Picasso.get().load("http://i.imgur.com/IjMSpbA.jpg").fit().into(profile_image)
            owner_name.visibility = View.INVISIBLE
        }

    }

    private fun setRecycleView(view: View) {

        val adapter = GroupAdapter<com.xwray.groupie.GroupieViewHolder>()

        adapter.add(MediaItem())
        adapter.add(MediaItem())
        adapter.add(MediaItem())
        adapter.add(MediaItem())

        media_grid.adapter = adapter;

        adapter.apply {
            val gridLayoutManager = GridLayoutManager(
                context,
                3,
                GridLayoutManager.VERTICAL,
                false
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    class MediaItem() : Item() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            //viewHolder.itemView.background = post.path
        }

        override fun getLayout() : Int = R.layout.profile_media_thumbnail
    }

    private fun fetchUserPosts() {
        //TODO: fetch the posts for the user and show in grid,

    }

    interface OnProfileSelected {
        fun onProfileSelected(profileModel: ProfileViewModel)
    }


}



