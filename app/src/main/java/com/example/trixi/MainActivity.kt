package com.example.trixi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trixi.entities.Post
import com.example.trixi.repository.DeleteFromDb
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.explore.ShowTopPostsFragment
import com.example.trixi.ui.fragments.UploadFragment
import com.example.trixi.ui.fragments.SinglePostFragment
import com.example.trixi.ui.home.HomepageFragment
import com.example.trixi.ui.profile.LoggedInUserProfileFragment
import com.example.trixi.ui.profile.PetRegister
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val homepageFragment = HomepageFragment()
        val postFragment = UploadFragment()
        val explorerFragment = ShowTopPostsFragment()
        val profileFragment = LoggedInUserProfileFragment()

        if (PostToDb.postedPost != null) {
            post = PostToDb.postedPost
            var singleFragment = SinglePostFragment(post)
            makeCurrentFragment(singleFragment)
        } else if (DeleteFromDb.postDeleted) {
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
            }
            true
        }

    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
}


