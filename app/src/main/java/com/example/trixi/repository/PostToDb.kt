package com.example.trixi.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostToDb {

    var loggedInUser: User? = null

    fun PostRegisterUserToDb(user: User){
        val retrofitClient = RetrofitClient.getRetroInstance().create(Api::class.java)

        val call = retrofitClient.createUser(user)
     call.enqueue(object : Callback<User> {
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
        val retrofitClient = RetrofitClient.getRetroInstance().create(Api::class.java)

        val call = retrofitClient.loginUser(user)
        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("uus", "login-user : onfailure " + t.message)
            }
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if(response.isSuccessful){
                    Log.d("uus", "login-user : onResponse success" + response.message())
                    GetLoggedInUserFromDB()
                }else{
                    Log.d("uus", "login-user : onResponse else")
                }
            }
        })

    }


    fun GetLoggedInUserFromDB(){
        val retrofitClient = RetrofitClient.getRetroInstance().create(Api::class.java)

        val call = retrofitClient.getLoggedInUser()
        call.enqueue(object : Callback<User> {
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
                } else {

                }
            }
        })
    }

}