package com.example.trixi.ui.profile

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.*

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SnapHelper

import com.example.marvelisimo.adapter.ProfileMediaGridAdapter
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.Pet
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.fragments.DrawerMenuFragment
import com.example.trixi.ui.fragments.SinglePostFragment
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class LoggedInUserProfileFragment : Fragment() {
    private var loggedInUser: User? = PostToDb.loggedInUser
    var toggleHamMenu: Boolean = false
    private lateinit var model: TrixiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        populateProfile()
        getPosts()
        getPets()
    }

    private fun getPets() {
        val snapHelper2: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper2.attachToRecyclerView(users_pet_list)

        users_pet_list.apply {
            if (!loggedInUser?.pets?.isEmpty()!!) {
                //set pet list for user if not empty
                users_pet_list.layoutManager = GridLayoutManager(
                    context,
                    1,
                    GridLayoutManager.HORIZONTAL,
                    false
                )
                adapter = ProfilePetListAdapter(loggedInUser!!.pets!!) { pet ->
                    redirectToPetProfile(pet)
                }
                users_pet_list.adapter = adapter
            }
        }
    }

    private fun getPosts() {

        val snapHelper1: SnapHelper = GravitySnapHelper(Gravity.TOP)
        snapHelper1.attachToRecyclerView(media_grid)

        if (loggedInUser?.posts.isNullOrEmpty()) {
            profile_no_posts.visibility = TextView.VISIBLE
        } else {
            media_grid.apply {

                model.getPostsByOwner(loggedInUser?.uid.toString())
                    ?.observe(viewLifecycleOwner, { posts ->
                        media_grid.layoutManager = GridLayoutManager(
                            context,
                            3,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                        adapter = ProfileMediaGridAdapter(posts as ArrayList<Post>) {
                            redirectToSinglePost(it)

                        }

                    })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_nav_menu, menu)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Profile"
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu -> {
                println("ISCLICKED")
                var manager: FragmentManager = requireActivity().supportFragmentManager
                var drawmenu = DrawerMenuFragment()

                if (!toggleHamMenu) {
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
        profile_bio.text = loggedInUser!!.bio
        Picasso.get().load(BASE_URL + loggedInUser!!.imageUrl).centerCrop().fit()
            .into(user_profile_pet_image)
        profile_following.text =
            "Following " + (loggedInUser!!.followingsPet?.size?.plus(loggedInUser!!.followingsUser!!.size)).toString()
        profile_followers.text = loggedInUser!!.followers?.size.toString() + " Followers"

        owner_name.visibility = View.INVISIBLE
        follow_button.visibility = View.INVISIBLE

    }

    private fun redirectToSinglePost(post: Post) {
        val singlePost = SinglePostFragment(post)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, singlePost)?.commit()
    }


    private fun redirectToPetProfile(pet: Pet) {
        val fm = activity?.supportFragmentManager

        val petProfile = PetProfileFragment(pet)
        fm?.beginTransaction()?.replace(R.id.fragment_container, petProfile)?.commit()
    }

}








