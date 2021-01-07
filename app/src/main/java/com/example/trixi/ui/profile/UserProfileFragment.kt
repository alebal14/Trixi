package com.example.trixi.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.example.marvelisimo.adapter.ProfileMediaGridAdapter
import com.example.trixi.R
import com.example.trixi.R.drawable.*
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.Pet
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.example.trixi.ui.fragments.SinglePostFragment
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.users_pet_list
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlin.math.log


class UserProfileFragment(val user: User?) : Fragment() {

    private lateinit var model: TrixiViewModel
    private var followed: Boolean = false
    private var numberOfFollowers = 0

    companion object {
        private val TAG = "profile"
        private val db = PostToDb()
        private var loggedInUser: User? = PostToDb.loggedInUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "username > ${user?.userName}")
        model = ViewModelProvider(this).get(TrixiViewModel::class.java)
        numberOfFollowers = user?.followers?.size!!

        checkIfFollowing()
        populateProfile()
        follow_button.setOnClickListener { handleFollow() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        if (user != null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = user.userName
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun populateProfile() {

        //TODO: Check username length and make text-size smaller if too long
        profile_name.text = user!!.userName
        profile_bio.text = user.bio
        Picasso.get().load(BASE_URL + user.imageUrl).centerCrop().fit()
            .into(user_profile_pet_image)
        profile_following.text =
            "Following " + (user.followingsPet?.size?.plus(user.followingsUser!!.size)).toString()
        profile_followers.text = numberOfFollowers.toString() + " Followers"
        owner_name.visibility = View.INVISIBLE

        getPets()
        getPosts()
    }

    private fun getPets() {

        val snapHelper2: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper2.attachToRecyclerView(users_pet_list)

        user?.uid?.let {
            model.getPetsByOwner(it)?.observe(viewLifecycleOwner, { pets ->
                Log.d(TAG, "size: pets  : ${pets?.size}")

                users_pet_list.apply {
                    if (!pets?.isEmpty()!!) {
                        //set pet list for user if not empty
                        users_pet_list.layoutManager = GridLayoutManager(
                            context,
                            1,
                            GridLayoutManager.HORIZONTAL,
                            false
                        )

                        adapter = ProfilePetListAdapter(pets as ArrayList<Pet>) { pet ->
                            redirectToPetProfile(pet)
                        }
                        users_pet_list.adapter = adapter
                    } else
                        users_pet_list.visibility = View.GONE
                }
            })
        }
    }


    private fun getPosts() {

        val snapHelper1: SnapHelper = GravitySnapHelper(Gravity.TOP)
        snapHelper1.attachToRecyclerView(media_grid)


        user?.uid?.let {
            model.getPostsByOwner(it)?.observe(viewLifecycleOwner, Observer { posts ->
                if (posts.isNullOrEmpty()) {
                    profile_no_posts.visibility = TextView.VISIBLE
                } else {
                    Log.d(TAG, "size: posts : ${posts?.size}")
                    profile_no_posts.visibility = View.GONE

                    media_grid.apply {
                        media_grid.layoutManager = GridLayoutManager(
                            context,
                            3,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                        adapter = ProfileMediaGridAdapter(posts as ArrayList<Post>) {
                            redirectToSinglePost(it)

                        }
                        //media_grid.adapter = ProfileMediaGridAdapter(posts as ArrayList<Post>
                    }

                }

            })
        }

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

    private fun toggleFollowIcon(followed: Boolean) {

        if (followed) follow_button.setBackgroundResource(ic_heart_filled)
        else follow_button.setBackgroundResource(ic_follow)
        Log.d("Profile", "not filled")
    }

    private fun handleFollow() {

        if (!followed) {
            Log.d("FOLLOW", "not followed; now following")
            loggedInUser?.let { db.follow(it.uid, user?.uid!!) }
            followed = true
            toggleFollowIcon(followed)
            numberOfFollowers += 1
            profile_followers.text = numberOfFollowers.toString() + " Followers"
        } else {
            Log.d("FOLLOW", "already followed; now unfollowing")
            loggedInUser?.let { db.unfollow(it.uid, user?.uid!!) }
            followed = false
            toggleFollowIcon(followed)
            numberOfFollowers -= 1
            profile_followers.text = numberOfFollowers.toString() + " Followers"
        }

    }

    private fun checkIfFollowing() {

        model.getOneUser(loggedInUser?.uid!!)?.observe(viewLifecycleOwner, Observer {
            it?.followingsUser?.forEach { followingUser ->
                if (followingUser.uid == user?.uid) {
                    followed = true
                }
                toggleFollowIcon(followed)
            }
        })
    }


}


