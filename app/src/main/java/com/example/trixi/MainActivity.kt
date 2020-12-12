package com.example.trixi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trixi.ui.fragments.PostFragment
import com.example.trixi.ui.fragments.SearchFragment
import com.example.trixi.ui.home.HomepageFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val homepageFragment = HomepageFragment()
         val postFragment = PostFragment()
         val searchFragment = SearchFragment()

         makeCurrentFragment(homepageFragment)

         bottom_nav.setOnNavigationItemSelectedListener {
             when(it.itemId){
                 R.id.footer_home -> makeCurrentFragment(homepageFragment)
                 R.id.footer_search -> makeCurrentFragment(searchFragment)
                 R.id.footer_post -> makeCurrentFragment(postFragment)

                 

             }
             true
         }



    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container,fragment)
            commit()
        }
    }


