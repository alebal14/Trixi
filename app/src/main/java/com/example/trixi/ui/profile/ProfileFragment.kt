package com.example.trixi.ui.profile

import android.app.Activity
import android.content.Context
import android.net.sip.SipSession
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.*
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.marvelisimo.adapter.ProfileMediaGridAdapter
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.DrawerMenuFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_btn_gallery_camera_toolbar.*
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
    var toggleHamMenu:Boolean = false

    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_nav_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Profile")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu -> {
                println("ISCLICKED")
                var manager: FragmentManager = requireActivity().supportFragmentManager
                var drawmenu = DrawerMenuFragment()

                if (toggleHamMenu == false){
                    toggleHamMenu = true
                    manager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                        R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.menuFragmentHolder, drawmenu)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }else{
                    toggleHamMenu = false
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }



    private fun populatePetList() {
        TODO("Populate list with users pets")
    }

    private fun populateProfile() {

            profile_name.text = loggedInUser!!.userName
            profile_bio.text = loggedInUser.bio
            Picasso.get().load(RetrofitClient.BASE_URL + loggedInUser.imageUrl).fit().into(profile_image)
            //use your ip here
            Picasso.get().load("http://192.168.1.71:3000/" + loggedInUser.imageUrl).fit().into(profile_image)
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






