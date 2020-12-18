package com.example.trixi.ui.profile

import android.os.Bundle
import android.view.*
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.example.marvelisimo.adapter.ProfileMediaGridAdapter
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.DrawerMenuFragment
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class LoggedInUserProfileFragment : Fragment() {

    val loggedInUser: User? = PostToDb.loggedInUser
    var toggleHamMenu: Boolean = false

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
            if (!loggedInUser?.posts?.isEmpty()!!) {
                media_grid.layoutManager = GridLayoutManager(
                    context,
                    3,
                    GridLayoutManager.VERTICAL,
                    false
                )
                media_grid.adapter = ProfileMediaGridAdapter(loggedInUser.posts)
            } else profile_no_posts.visibility = TextView.VISIBLE
        }

        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(users_pet_list)

        users_pet_list.apply {
            if (!loggedInUser?.pets?.isEmpty()!!) {
                //set pet list for user if not empty
                users_pet_list.layoutManager = GridLayoutManager(
                    context,
                    1,
                    GridLayoutManager.HORIZONTAL,
                    false
                )
                adapter = ProfilePetListAdapter(loggedInUser.pets)
                users_pet_list.adapter = adapter
            }
        }
        populateProfile()
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

    private fun populateProfile() {

        profile_name.text = loggedInUser!!.userName
        profile_bio.text = loggedInUser.bio
        Picasso.get().load(BASE_URL + loggedInUser.imageUrl).fit().into(user_profile_pet_image)
        profile_following.text = "Following " + (loggedInUser.followingsPet?.size?.plus(loggedInUser.followingsUser!!.size)).toString()
        profile_followers.text= loggedInUser.followers?.size.toString() + " Followers"

        owner_name.visibility = View.INVISIBLE
        follow_button.visibility = View.INVISIBLE

    }

}








