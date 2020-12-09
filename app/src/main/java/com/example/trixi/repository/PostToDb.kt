package com.example.trixi.repository

import android.util.Log
import android.widget.Toast
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient

import com.example.trixi.entities.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostToDb {


    fun makeApiPost(user: User){
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

}