package com.example.trixi

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trixi.repository.GetFromDbViewModel
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.fragments.PostFragment
import com.example.trixi.ui.fragments.SearchFragment
import com.example.trixi.ui.fragments.UploadFragment
import com.example.trixi.ui.home.HomepageFragment
import com.example.trixi.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NetworkStateReceiver.ConnectivityReceiverListener {


    val model: GetFromDbViewModel by viewModels()

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
         //RetrofitClient.context = this

        //setContentView(R.layout.fragment_share)

         registerReceiver(
             NetworkStateReceiver(),
             IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
         )


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



    private fun showMessage(isConnected: Boolean) {
        if (!isConnected) {
            Toast.makeText(
                applicationContext,
                "Internet Not Available",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Log.d("networkaccess", "connected")
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        NetworkStateReceiver.connectivityReceiverListener = this
    }
    
    }




