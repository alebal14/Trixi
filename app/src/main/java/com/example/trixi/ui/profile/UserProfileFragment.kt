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
import com.example.trixi.R
import com.example.trixi.R.drawable.*
import com.example.trixi.apiService.RetrofitClient.Companion.BASE_URL
import com.example.trixi.entities.Pet
import com.example.trixi.entities.User
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.users_pet_list
import kotlinx.android.synthetic.main.fragment_profile.view.*


class UserProfileFragment(val user: User?) : Fragment() {

    private lateinit var model: TrixiViewModel
    private var followed: Boolean = false

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
        profile_name.text = user!!.userName
        profile_bio.text = user.bio
        Picasso.get().load(BASE_URL + user.imageUrl).centerCrop().fit()
            .into(user_profile_pet_image)
        profile_following.text =
            "Following " + (user.followingsPet?.size?.plus(user.followingsUser!!.size)).toString()
        profile_followers.text = user.followers?.size.toString() + " Followers"
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
                    }
                }
            })
        }
    }


    private fun getPosts() {

        val snapHelper1: SnapHelper = GravitySnapHelper(Gravity.TOP)
        snapHelper1.attachToRecyclerView(media_grid)

        if (user?.posts.isNullOrEmpty()) {
            profile_no_posts.visibility = TextView.VISIBLE
        } else {
            user?.uid?.let {
                model.getPostsByOwner(it)?.observe(viewLifecycleOwner, Observer { posts ->
                    Log.d(TAG, "size: posts : ${posts?.size}")

                    media_grid.apply {
                        media_grid.layoutManager = GridLayoutManager(
                            context,
                            3,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                        //media_grid.adapter = ProfileMediaGridAdapter(posts as ArrayList<Post>
                    }
                })
            }
        }
    }

    private fun redirectToPetProfile(pet: Pet) {
        val fm = activity?.supportFragmentManager

        val petProfile = PetProfileFragment(pet)
        if (fm != null) {
            fm.beginTransaction().replace(R.id.fragment_container, petProfile).commit()
        }
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
        }

        else {
            Log.d("FOLLOW", "already followed; now unfollowing")
            loggedInUser?.let { db.unfollow(it.uid, user?.uid!!) }
            followed = false
            toggleFollowIcon(followed)
        }

        //refreshFragment()

        //TODO: REFRESH FRAGMENT? ATTACH/DETACH?
    }

    private fun refreshFragment() {
        val fm = activity?.supportFragmentManager!!
        fm.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }

    private fun checkIfFollowing() {
        //TODO: get one user by id (loggedIn) -> updated followings list

        loggedInUser?.uid?.let { it ->
            model.getOneUser(it)?.observe(viewLifecycleOwner, Observer { loggedInU ->
                loggedInU?.followingsUser?.forEach{
                    if (it.uid == user?.uid) {
                        followed = true
                    }
                    toggleFollowIcon(followed)
                }
            })
        }
    }

}
