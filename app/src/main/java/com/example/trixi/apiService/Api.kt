@file:JvmName("ApiKt")
package com.example.trixi.apiService

import com.example.trixi.entities.Post
import com.example.trixi.entities.User

import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("rest/users")
    fun createUser(@Body user: User): Call<User>

    @POST("rest/login")
    fun loginUser(@Body user: User): Call<User>

    @GET("rest/login")
    fun getLoggedInUser(): Call<User>

    @GET("rest/users")
    fun getAllUsers(): Call<List<User>>

    @GET("api/getUserFollowingPost/{id}")
    fun getFollowingsPost(@Path(value="id") id : String?):Call<List<Post>>


}