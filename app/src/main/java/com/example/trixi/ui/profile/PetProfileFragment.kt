package com.example.trixi.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
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
import com.example.trixi.ui.fragments.PopUpFollowWindow
import com.example.trixi.ui.post.SinglePostFragment
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class PetProfileFragment(val pet: Pet?) : Fragment() {

    private lateinit var model: TrixiViewModel
    private var followed = false
    private lateinit var owner: User
    private var numberOfFollowers = 0
    private var ownerIsLoggedInUser = false
    var headerText = ""

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

        numberOfFollowers = pet?.followers?.size!!

        handleClickOnFollow(pet)
        handleClickEdit(pet)

        getPetOwner()
        checkIfFollowing()
        follow_button.setOnClickListener { handleFollow() }
        populateProfile()
    }

    private fun handleClickOnFollow(pet: Pet) {

        if(pet.followers?.size.toString() != "0"){
            profile_followers.setOnClickListener {
                headerText =  pet.name.toString() + "'s followers"
                val popUp = PopUpFollowWindow( activity?.supportFragmentManager!!,headerText, pet.followers, null)
                popUp.show(activity?.supportFragmentManager!!, PopUpFollowWindow.TAG)

            }
        }
    }

    private fun handleClickEdit(pet: Pet) {
        edit_pet.setOnClickListener {
            val editPet = EditPetProfile(pet)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, editPet)?.commit()
        }
    }

    private fun populateProfile() {
        profile_name.text = pet!!.name
        profile_bio.text = pet.bio
        Picasso.get().load(RetrofitClient.BASE_URL + pet.imageUrl).centerCrop().fit()
            .into(user_profile_pet_image)
        users_pet_list.visibility = GONE
        profile_followers.text = pet.followers?.size.toString() + " Followers"
        profile_followers.gravity = TEXT_ALIGNMENT_CENTER
        profile_following.visibility = INVISIBLE
        getPosts()
    }

    private fun getPetOwner() {
        owner_name.visibility = VISIBLE

        pet?.ownerId?.let {
            model.getOneUser(it)?.observe(viewLifecycleOwner, { owner ->
                setPetOwner(owner)
            }
            )
        }
    }

    private fun setPetOwner(user: User) {
        owner = user

        if (owner.uid == loggedInUser?.uid) {
            ownerIsLoggedInUser = true
            follow_button.visibility = GONE
            edit_pet.visibility = VISIBLE
        }

        owner_name.setOnClickListener { redirectToOwner(owner) }
        owner_name.text = "Owner: " + owner.userName
    }

    private fun redirectToOwner(owner: User?) {
        if (ownerIsLoggedInUser) {
            val ownersProfile = LoggedInUserProfileFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, ownersProfile)?.commit()
        }else
        model.getOneUser(owner?.uid!!)?.observe(viewLifecycleOwner, { owner ->
            val ownersProfile = UserProfileFragment(owner)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, ownersProfile)?.commit()
        })
    }

    private fun getPosts() {

        val snapHelper1: SnapHelper = GravitySnapHelper(Gravity.TOP)
        snapHelper1.attachToRecyclerView(media_grid)

        pet?.uid?.let  {
            model.getPostsByOwner(it)?.observe(viewLifecycleOwner, { posts ->
                if (posts.isNullOrEmpty()) {
                    profile_no_posts.text = "No posts yet"
                    profile_no_posts.visibility = TextView.VISIBLE
                } else {
                    profile_no_posts.visibility = View.GONE

                    val postMedia = posts!!.filter { post -> post.fileType!!.contains("image")}

                    pics_videos_tab.addOnTabSelectedListener(object :
                        TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab) {

                            when (tab.position) {
                                0 -> {
                                    val postImage = posts!!.filter { post -> post.fileType!!.contains("image") }
                                    if(postImage.isNullOrEmpty()){
                                        profile_no_posts.text = "No images yet"
                                        profile_no_posts.visibility = TextView.VISIBLE
                                        media_grid.adapter = null

                                    } else {
                                        profile_no_posts.visibility = View.GONE
                                        media_grid.apply {
                                            media_grid.layoutManager = GridLayoutManager(
                                                context,
                                                3,
                                                GridLayoutManager.VERTICAL,
                                                false
                                            )
                                            adapter = ProfileMediaGridAdapter(postImage as ArrayList<Post>) {
                                                redirectToSinglePost(it)
                                            }
                                        }
                                    }
                                }
                                1 -> {
                                    val postVideo = posts!!.filter { post -> post.fileType!!.contains("video")}
                                    if(postVideo.isNullOrEmpty()){
                                        profile_no_posts.text = "No videos yet"
                                        profile_no_posts.visibility = TextView.VISIBLE
                                        media_grid.adapter = null
                                    } else {
                                        profile_no_posts.visibility = View.GONE
                                        media_grid.apply {
                                            media_grid.layoutManager = GridLayoutManager(
                                                context,
                                                3,
                                                GridLayoutManager.VERTICAL,
                                                false
                                            )
                                            adapter = ProfileMediaGridAdapter(postVideo as ArrayList<Post>) {
                                                redirectToSinglePost(it)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        override fun onTabUnselected(tab: TabLayout.Tab) {}
                        override fun onTabReselected(tab: TabLayout.Tab) {}
                    })
                    media_grid.apply {
                        media_grid.layoutManager = GridLayoutManager(
                            context,
                            3,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                        adapter = ProfileMediaGridAdapter(postMedia as ArrayList<Post>) {
                            redirectToSinglePost(it)
                        }
                    }
                }
            })
        }
    }

    private fun redirectToSinglePost(post: Post) {
        val singlePost = SinglePostFragment(post, null)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, singlePost)?.addToBackStack("singelPostFragment")!!.commit()

    }

    private fun toggleFollowIcon(followed: Boolean) {

        if (followed) follow_button.setBackgroundResource(R.drawable.ic_heart_filled)
        else follow_button.setBackgroundResource(R.drawable.ic_follow)
        Log.d("PET PROFILE", "heart button")
    }

    private fun handleFollow() {

        if (!followed) {
            loggedInUser?.let { db.follow(it.uid, pet?.uid!!) }
            followed = true
            toggleFollowIcon(followed)
            numberOfFollowers += 1
            profile_followers.text = numberOfFollowers.toString() + " Followers"
        } else {
            loggedInUser?.let { db.unfollow(it.uid, pet?.uid!!) }
            followed = false
            toggleFollowIcon(followed)
            numberOfFollowers -= 1
            profile_followers.text = numberOfFollowers.toString() + " Followers"
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

    override fun onDestroyView() {
        super.onDestroyView()
        PostToDb.createdPet = null
    }

}
