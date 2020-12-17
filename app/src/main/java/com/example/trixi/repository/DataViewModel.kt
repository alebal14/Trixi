package com.example.trixi.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataViewModel : ViewModel() {

    // TODO: 2020-12-16  get All Post, get all Post by ownerId, get posts by category ID
    // TODO: get all category, get all pet types
    // TODO: 2020-12-16 get all pets, get pets by pet Types, get pet by pet_id , get pet by owner_id 


    fun getAllUsersFromDB(): MutableLiveData<List<User>> {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        var userListData: MutableLiveData<List<User>> = MutableLiveData()
        val call = retrofitClient?.getAllUsers()
        call?.enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("uus", "users : onfailure " + t.message)
                userListData.postValue(null)
            }

            override fun onResponse(
                call: Call<List<User>>, response: Response<List<User>>
            ) {
                if (response.isSuccessful) {
                    userListData.postValue(response.body())
                } else {
                    userListData.postValue(null)
                }
            }
        })
        return userListData
    }

    //    fun GetLatestPostFromDB(id :String){
//        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
//
//        val call = retrofitClient?.getLatestPost(id)
//        call?.enqueue(object : Callback<Post> {
//            override fun onFailure(call: Call<Post>, t: Throwable) {
//
//            }
//            override fun onResponse(
//                call: Call<Post>, response: Response<Post>
//            ) {
//                if (response.isSuccessful) {
//                    Log.d("latestPost SuccessFul", "Latest Post")
//                    latestPost = response.body()
//                    Log.d("latestPost SuccessFul", latestPost.toString())
//
//                } else {
//                    Log.d("latestPost", "Latest Post")
//                }
//            }
//        })
//    }

    fun getFollowingsPostFromDb(id: String?): MutableLiveData<List<Post>> {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        var followingsPost: MutableLiveData<List<Post>> = MutableLiveData();
        val call = retrofitClient?.getFollowingsPost(id)
        call?.enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d("post", "posts : onfailure " + t.message)
                followingsPost.postValue(null)
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    followingsPost.postValue(response.body())

                } else {
                    followingsPost.postValue(null)
                }
            }
        })
        return followingsPost;

    }

    fun getOneUserFromDb(id: String?): MutableLiveData<User> {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        val user: MutableLiveData<User> = MutableLiveData()
        val call = retrofitClient?.getUserById(id)
        call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("uus", "user : onfailure " + t.message)
                user.postValue(null)
            }

            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if (response.isSuccessful) {
                    Log.d("uus", "success")
                    user.postValue(response.body())
                } else {
                    user.postValue(null)
                }

            }

        })
        return user

    }



}