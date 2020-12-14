package com.example.trixi.ui.profile

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.trixi.R
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.DrawerMenuFragment
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_btn_gallery_camera_toolbar.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_media_item.view.*

class ProfileFragment : Fragment() {

    val loggedInUser: User? = PostToDb.loggedInUser
    var toggleHamMenu:Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        populateMediaGrid()

    }

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

        populateProfile()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_nav_menu, menu)
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
            R.id.edit_user ->{
                println("EDIISCLICKED")
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun populateProfile() {

        if(loggedInUser != null) {
            profile_name.text = loggedInUser.userName
            profile_bio.text = loggedInUser.bio
            //use your ip here
            Picasso.get().load("http://192.168.1.71:3000/" + loggedInUser.imageUrl).fit().into(profile_image)
            owner_name.visibility = View.INVISIBLE

            //TODO: get followers & following
//            profile_followers
//            profile_following
        }
    }

    private fun populateMediaGrid() {

       /* val adapter = GroupAdapter<GroupieViewHolder>()
        media_grid.layoutManager = GridLayoutManager(context, 4)

        adapter.add(MediaItem())
        adapter.add(MediaItem())
        adapter.add(MediaItem())
        adapter.add(MediaItem())

        media_grid.adapter = adapter*/

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



