package com.example.trixi.ui.profile

import android.os.Bundle
import android.view.*
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
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
import com.example.trixi.ui.fragments.PopUpFollowWindow
import com.example.trixi.ui.fragments.SinglePostFragment
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class LoggedInUserProfileFragment : Fragment() {
    private var loggedInUser: User? = PostToDb.loggedInUser
    var toggleHamMenu: Boolean = false
    private lateinit var model: TrixiViewModel
    var headerText = ""


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

        model.getOneUser(loggedInUser?.uid!!)
            ?.observe(viewLifecycleOwner, {
                populateProfile(it)
                handleClickOnFollow(it)
            })

        getPosts()
        getPets()


    }

    private fun handleClickOnFollow(user: User) {
        profile_followers.setOnClickListener {
                headerText = "Your followers"
                val popUp = PopUpFollowWindow( activity?.supportFragmentManager!!,headerText,user.followers, null)
                popUp.show(activity?.supportFragmentManager!!, PopUpFollowWindow.TAG)

        }

        profile_following.setOnClickListener {
                headerText = "You are following"
                val popUp = PopUpFollowWindow(activity?.supportFragmentManager!!, headerText, user.followingsUser, user.followingsPet)
                popUp.show(activity?.supportFragmentManager!!, PopUpFollowWindow.TAG)
            
        }
    }

    private fun getPets() {
        val snapHelper2: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper2.attachToRecyclerView(users_pet_list)
        loggedInUser?.uid?.let {
            model.getPetsByOwner(it)?.observe(viewLifecycleOwner, { pets ->

                users_pet_list.apply {
                    if (pets.isNotEmpty()) {
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
                    else
                        users_pet_list.visibility = View.GONE
                }
            })

        }
    }

    private fun getPosts() {

        val snapHelper1: SnapHelper = GravitySnapHelper(Gravity.TOP)
        snapHelper1.attachToRecyclerView(media_grid)

        loggedInUser?.uid?.let {
            model.getPostsByOwner(it)?.observe(viewLifecycleOwner, { posts ->
                if (posts.isNullOrEmpty()) {
                    profile_no_posts.visibility = TextView.VISIBLE
                } else {
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
                        .addToBackStack("drawer")
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

    private fun populateProfile(updatedLoggedInUser: User) {

        follow_button.visibility = View.INVISIBLE
        profile_name.text = updatedLoggedInUser!!.userName
        profile_bio.text = updatedLoggedInUser!!.bio
        Picasso.get().load(BASE_URL + updatedLoggedInUser!!.imageUrl).centerCrop().fit()
            .into(user_profile_pet_image)
        profile_following.text =
            "Following " + (updatedLoggedInUser!!.followingsPet?.size?.plus(updatedLoggedInUser!!.followingsUser!!.size)).toString()
        profile_followers.text = updatedLoggedInUser!!.followers?.size.toString() + " Followers"

        owner_name.visibility = View.INVISIBLE
        follow_button.visibility = View.INVISIBLE

    }

    private fun redirectToSinglePost(post: Post) {
        val singlePost = SinglePostFragment(post)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, singlePost)?.addToBackStack("singelPostFragment")!!.commit()
    }


    private fun redirectToPetProfile(pet: Pet) {
        val fm = activity?.supportFragmentManager

        val petProfile = PetProfileFragment(pet)
        fm?.beginTransaction()?.replace(R.id.fragment_container, petProfile)?.addToBackStack("petprofileFragment")!!.commit()
    }

}








