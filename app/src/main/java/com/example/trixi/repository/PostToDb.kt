package com.example.trixi.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.User
import com.example.trixi.ui.register.RegisterActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostToDb {

    companion object {
        var loggedInUser: User? = null
    }


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

    fun PostImageToServer() {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.uploadProfileImage()
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Image", "Image : onfailure " + t.message)
            }
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if(response.isSuccessful){
                    Log.d("Image", "Image : onResponse success" + response.message())
                }else{
                    Log.d("Image", "Image : onResponse else" + response.message())
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