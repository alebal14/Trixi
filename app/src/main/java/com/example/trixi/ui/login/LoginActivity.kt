package com.example.trixi.ui.login

import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

import com.example.trixi.NetworkStateReceiver

import com.example.trixi.R
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.User
import com.example.trixi.repository.GetFromDbViewModel
import com.example.trixi.repository.PostToDb
import com.example.trixi.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), NetworkStateReceiver.ConnectivityReceiverListener {

    val model: GetFromDbViewModel by viewModels()
    val post = PostToDb()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        RetrofitClient.context = this
        post.GetLoggedInUserFromDB(this)

        var background = findViewById<ConstraintLayout>(R.id.layout)
        var animation = background.background as AnimationDrawable

        animation.setEnterFadeDuration(5000);
        animation.setExitFadeDuration(5000)

        animation.start()


        registerReceiver(
            NetworkStateReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        login_no_account.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            val usernameOrEmail = login_input_username_or_password.text.toString()
            val password = login_input_password.text.toString()

            if (usernameOrEmail.contains("@")) {
                val user = User("", "", usernameOrEmail, password, "", "", "", null, null)
                post.PostLoginUserToDb(user, this)
            } else {
                val user = User("", usernameOrEmail, "", password, "", "", "", null, null)
                post.PostLoginUserToDb(user, this)
            }
        }



        //testObserver getAllUsers:

        /*

        model.GetAllUsersFromDB()
        model.getUserMutableLiveDataList().observe(this, Observer {
            it.forEach {
                Log.d("uus", "UserName : ${it.userName!!}")
            }
        })

         */

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

        /*model.GetAllUsersFromDB()
        model.getUserMutableLiveDataList().observe(this, Observer{
            it.forEach{
                Log.d("uus", "UserName : ${it.userName!!}")
            }
        } )*/


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        NetworkStateReceiver.connectivityReceiverListener = this
    }




}



