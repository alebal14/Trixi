package com.example.trixi.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity

import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.User
import com.example.trixi.ui.login.LoginActivity
import com.example.trixi.ui.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostToDb {

    var loggedInUser: User? = null

    fun PostRegisterUserToDb(user: User){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.createUser(user)
     call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("uus", "REG-user : onfailure " + t.message)

            }
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if(response.isSuccessful){
                    Log.d("uus", "REGuser : onResponse success" + response.body())
                }else{
                    Log.d("uus", "REGuser : onResponse else" + response.body())
                }
            }
        })
    }

    fun PostLoginUserToDb(user: User, context: Context){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.loginUser(user)
        call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("uus", "login-user : onfailure: " + t.message)
                Log.d("uus", "login-user : onfailure: Username/email does not exist")
                Toast.makeText(context, "Username/email does not exist", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if(response.isSuccessful){
                    Log.d("uus", "login-user : onResponse success" + response.message())
                    GetLoggedInUserFromDB(context)
                }else{
                    Log.d("uus", "login-user : onResponse else: password and username/email dont match")
                    Toast.makeText(context, "Password and username/email dont match", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    fun GetLoggedInUserFromDB(context: Context){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.getLoggedInUser()
        call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("uus", "loggedInUser : onfailure " + t.message)
            }
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if (response.isSuccessful) {
                    Log.d("loggedInUser", "success")
                    loggedInUser = response.body()
                    Log.d("loggedInUser", loggedInUser.toString())

                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)

                } else {

                }
            }
        })
    }

}