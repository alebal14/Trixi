package com.example.trixi.ui.profile

import android.net.sip.SipSession
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelisimo.adapter.ProfileMediaGridAdapter
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_home_item.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.media_grid
import kotlinx.android.synthetic.main.media_grid.*
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

        media_grid.layoutManager = GridLayoutManager(context,4)

        //media_grid.addItemDecoration(GridItemDecoration(10, 2))
        if (loggedInUser != null) {
            if(!loggedInUser.posts?.isEmpty()!!) {
                val mediaGridAdapter = ProfileMediaGridAdapter(loggedInUser.posts)
                media_grid.adapter = mediaGridAdapter
            }
            else profile_no_posts.visibility = TextView.VISIBLE

            populateProfile()
        }
        //populateMediaGrid()
        //populatePetList()
    }

    private fun populatePetList() {
        TODO("Populate list with users pets")
    }

    private fun populateProfile() {

            profile_name.text = loggedInUser!!.userName
            profile_bio.text = loggedInUser.bio
            Picasso.get().load(RetrofitClient.BASE_URL + loggedInUser.imageUrl).fit().into(profile_image)
            owner_name.visibility = View.INVISIBLE

            //TODO: get followers & following
//            profile_followers
//            profile_following
        }
    }

    private fun fetchUserPosts() {
        //TODO: send posts to adapter
    }

//    interface OnProfileSelected {
//        fun onProfileSelected(profileModel: ProfileViewModel)
//    }






