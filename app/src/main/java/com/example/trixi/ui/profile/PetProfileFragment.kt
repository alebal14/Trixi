package com.example.trixi.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.example.marvelisimo.adapter.ProfileMediaGridAdapter
import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Pet
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.media_grid
import kotlinx.android.synthetic.main.fragment_profile.view.*

class PetProfileFragment(val pet: Pet?) : Fragment() {

    private lateinit var model: TrixiViewModel
    private var followed = false

    companion object {
        private val TAG = "petProfile"
        private val db = PostToDb()
        private var loggedInUser: User? = PostToDb.loggedInUser
    }

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

        checkIfFollowing()
        follow_button.setOnClickListener { handleFollow() }
        populateProfile()
    }


    private fun populateProfile() {
        profile_name.text = pet!!.name
        profile_bio.text = pet.bio
        Picasso.get().load(RetrofitClient.BASE_URL + pet.imageUrl).centerCrop().fit()
            .into(user_profile_pet_image)
        profile_followers.text = pet.followers?.size.toString() + " Followers"
        profile_followers.gravity = TEXT_ALIGNMENT_CENTER
        owner_name.visibility = VISIBLE
        owner_name.text = "Owner: " + getOwnerName()
        profile_following.visibility = INVISIBLE

        getPosts()
    }

    private fun getOwnerName(): String? {

        //TODO: funkar ej??
        var ownerName = ""

        pet?.ownerId?.let {
            model.getOneUser(it)?.observe(viewLifecycleOwner, { user ->
                ownerName = user.userName.toString()
            })
        }
        return ownerName
    }

    private fun getPosts() {

        val snapHelper1: SnapHelper = GravitySnapHelper(Gravity.TOP)
        snapHelper1.attachToRecyclerView(media_grid)

        if (pet?.posts.isNullOrEmpty()) {
            profile_no_posts.visibility = TextView.VISIBLE
        } else {
            pet?.uid?.let {
                model.getPostsByOwner(it)?.observe(viewLifecycleOwner, { posts ->
                    media_grid.apply {
                        media_grid.layoutManager = GridLayoutManager(
                            context,
                            3,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                        //media_grid.adapter = ProfileMediaGridAdapter(posts as ArrayList<Post>)
                    }
                })
            }
        }
    }

    private fun toggleFollowIcon(followed: Boolean) {

        if (followed) follow_button.setBackgroundResource(R.drawable.ic_heart_filled)
        else follow_button.setBackgroundResource(R.drawable.ic_follow)
        Log.d("PET PROFILE", "heart button")
    }

    private fun handleFollow() {

        if (!followed) {
            Log.d("FOLLOW PET", "not followed; now following")
            loggedInUser?.let { db.follow(it.uid, pet?.uid!!) }
            followed = true
            toggleFollowIcon(followed)
        } else {
            Log.d("FOLLOW PET", "already followed; now unfollowing")
            loggedInUser?.let { db.unfollow(it.uid, pet?.uid!!) }
            followed = false
            toggleFollowIcon(followed)
        }
    }

    private fun checkIfFollowing() {

        model.getOneUser(loggedInUser?.uid!!)?.observe(viewLifecycleOwner, { user ->
            user?.followingsPet?.forEach {
                if (it.uid == pet?.uid) {
                    followed = true
                }
                toggleFollowIcon(followed)
            }
        })
    }

}
