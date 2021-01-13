package com.example.trixi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trixi.entities.Pet
import com.example.trixi.entities.Post
import com.example.trixi.repository.DeleteFromDb
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.activity.ActivityFragment
import com.example.trixi.ui.explore.ShowTopPostsFragment
import com.example.trixi.ui.post.SinglePostFragment
import com.example.trixi.ui.post.UploadFragment
import com.example.trixi.ui.home.HomepageFragment
import com.example.trixi.ui.profile.LoggedInUserProfileFragment
import com.example.trixi.ui.profile.PetProfileFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var post: Post? = null
    var pet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val homepageFragment = HomepageFragment()
        val postFragment = UploadFragment()
        val explorerFragment = ShowTopPostsFragment()
        val profileFragment = LoggedInUserProfileFragment()
        val activityFragment = ActivityFragment()

        if (PostToDb.postedPost != null) {
            post = PostToDb.postedPost
            var singleFragment = SinglePostFragment(post)
            makeCurrentFragment(singleFragment)
        } else if (PostToDb.createdPet != null) {
            pet = PostToDb.createdPet
            var petFragment = PetProfileFragment(pet)
            makeCurrentFragment(petFragment)
        } else if (DeleteFromDb.postDeleted || DeleteFromDb.petDeleted || PostToDb.userBoolean) {
            makeCurrentFragment(profileFragment)
        } else {
            makeCurrentFragment(homepageFragment)
        }


        bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.footer_home -> makeCurrentFragment(homepageFragment)
                R.id.footer_search -> makeCurrentFragment(explorerFragment)
                R.id.footer_post -> makeCurrentFragment(postFragment)
                R.id.footer_profile -> makeCurrentFragment(profileFragment)
                R.id.footer_activity ->makeCurrentFragment(activityFragment)
            }
            true
        }

    }

    override fun onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment).addToBackStack("NavFragment").
            commit()
        }



}


