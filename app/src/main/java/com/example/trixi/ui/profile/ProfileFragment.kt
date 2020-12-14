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
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_media_item.view.*

class ProfileFragment : Fragment() {

    val loggedInUser: User? = PostToDb.loggedInUser

    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateProfile()
        populateMediaGrid()
    }

    private fun populateProfile() {

        if(loggedInUser != null) {
            profile_name.text = loggedInUser.userName
            profile_bio.text = loggedInUser.bio
            //use your ip here
            Picasso.get().load("http://192.168.8.101:3000/" + loggedInUser.imageUrl).fit().into(profile_image)
            owner_name.visibility = View.INVISIBLE

            //TODO: get followers & following
//            profile_followers
//            profile_following
        }
    }

    private fun populateMediaGrid() {

        val adapter = GroupAdapter<GroupieViewHolder>()
        media_grid.layoutManager = GridLayoutManager(context, 4)

        adapter.add(MediaItem())
        adapter.add(MediaItem())
        adapter.add(MediaItem())
        adapter.add(MediaItem())

        media_grid.adapter = adapter

    }

    class MediaItem() : Item() {
        //TODO make it post
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.apply {
                Picasso.get().load("https://imgur.com/IjMSpbA").into(media_item_thumbnail)
            }
        }
        override fun getLayout() : Int = R.layout.profile_media_item
    }

    private fun fetchUserPosts() {
        //TODO: fetch the posts for the user and show in grid,

    }

//    interface OnProfileSelected {
//        fun onProfileSelected(profileModel: ProfileViewModel)
//    }


}



