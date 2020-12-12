package com.example.trixi

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
//import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.User
import com.example.trixi.repository.GetFromDbViewModel
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.PostFragment
import com.example.trixi.ui.fragments.SearchFragment
import com.example.trixi.ui.home.HomepageFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val model: GetFromDbViewModel by viewModels()

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         //RetrofitClient.context = this
         val homepageFragment = HomepageFragment()
         val postFragment = PostFragment()
         val searchFragment = SearchFragment()

         print("main login-user :${PostToDb.loggedInUser}")

//         val bundle: Bundle = Bundle()
//         model.GetLoggedInUserFromDB().observe(this,{
//             if (it != null) {
//                 Log.d("uus","log in user from main activity. ${it.userName}")
//                 Log.d("uus","log in userId from main activity. ${it.uid}")
//
//
//                 bundle.putString("loggedInUserId", it.uid); }
//         })
//
//         homepageFragment.arguments = bundle

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


