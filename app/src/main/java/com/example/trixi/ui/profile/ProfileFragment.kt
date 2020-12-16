package com.example.trixi.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelisimo.adapter.ProfileMediaGridAdapter
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.DrawerMenuFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.media_grid
import kotlinx.android.synthetic.main.fragment_profile.profile_no_posts
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    val loggedInUser: User? = PostToDb.loggedInUser
    var toggleHamMenu: Boolean = false
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ProfileMediaGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        media_grid.apply {

            //set up post thumbnails for user or show text:"no posts"
            if (loggedInUser != null) {
                if (!loggedInUser.posts?.isEmpty()!!) {
                    media_grid.layoutManager = GridLayoutManager(context, 3)
                    adapter = ProfileMediaGridAdapter(loggedInUser.posts)
                    media_grid.adapter = adapter
                } else profile_no_posts.visibility = TextView.VISIBLE
                if(loggedInUser.pets?.isEmpty()!!){
                    users_pet_list.apply {
                        //set pest list for user if not empty
                    }
                }

                populateProfile()
            }
        }
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

                if (toggleHamMenu == false) {
                    toggleHamMenu = true
                    manager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                            R.anim.enter_left_to_right, R.anim.exit_left_to_right
                        )
                        .replace(R.id.menuFragmentHolder, drawmenu)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                } else {
                    toggleHamMenu = false
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }


    fun populatePetList() {
        TODO("Populate list with users pets")
    }

    private fun populateProfile() {

        profile_name.text = loggedInUser!!.userName
        profile_bio.text = loggedInUser.bio
        Picasso.get().load(BASE_URL + loggedInUser.imageUrl).fit().into(user_profile_pet_image)
        resources.getString(R.string.number_of_following, loggedInUser.followingsPet?.size?.plus(loggedInUser.followingsUser!!.size).toString())
        //getString(R.string.number_of_followers, loggedInUser.followers?.size.toString())
        //profile_following.text = "Following " + (loggedInUser.followingsPet.size + loggedInUser.followingsUser.size).toString()

        owner_name.visibility = View.INVISIBLE
        follow_button.visibility = View.INVISIBLE

        //TODO: get followers & following
//            profile_followers
//            profile_following
    }

}








