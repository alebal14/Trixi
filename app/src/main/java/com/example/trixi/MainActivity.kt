package com.example.trixi

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trixi.repository.DataViewModel
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.SearchFragment
import com.example.trixi.ui.fragments.UploadFragment
import com.example.trixi.ui.fragments.singlePostFragment
import com.example.trixi.ui.home.HomepageFragment
import com.example.trixi.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope


class MainActivity : AppCompatActivity() {


    val model: DataViewModel by viewModels()

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
         //RetrofitClient.context = this

        //setContentView(R.layout.fragment_share)
        val single = intent.getStringExtra("EXTRA")

         val homepageFragment = HomepageFragment()
         val postFragment = UploadFragment()
         val searchFragment = SearchFragment()
         val profileFragment = ProfileFragment()
         val singleFragment = singlePostFragment()

         val post = PostToDb.latestPost

         Log.d("post", " $post")


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

         if(single.isNullOrEmpty()){
             makeCurrentFragment(homepageFragment)
         } else {
             val bundle = Bundle()
             //bundle.putString("edttext", "From Activity")
             bundle.putString("title", post?.title.toString())
             bundle.putString("url", post?.filePath.toString())
             bundle.putString("description", post?.description.toString())
             singleFragment.arguments = bundle;
             makeCurrentFragment(singleFragment);
         }

         bottom_nav.setOnNavigationItemSelectedListener {
             when(it.itemId){
                 R.id.footer_home -> makeCurrentFragment(homepageFragment)
                 R.id.footer_search -> makeCurrentFragment(searchFragment)
                 R.id.footer_post -> makeCurrentFragment(postFragment)
                 R.id.footer_profile -> makeCurrentFragment(profileFragment)
             }
             true
         }

    }


    fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container,fragment)
            commit()
        }
    }


