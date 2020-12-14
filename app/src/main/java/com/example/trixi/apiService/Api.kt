@file:JvmName("ApiKt")
package com.example.trixi.apiService


import com.example.trixi.entities.Post

import com.example.trixi.entities.ProfileImage

import com.example.trixi.entities.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.MultipartBody


import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("rest/users")
    fun createUser(@Body user: User): Call<User>

    @POST("rest/login")
    fun loginUser(@Body user: User): Call<User>


    @Multipart
    @POST("rest/image")
    fun postProfileImage(
            @Part("file") file: String
    ):Call<ResponseBody>


    @POST("rest/posts")
    fun postPostToDb(@Body post: Post) : Call<Post>

    @GET("rest/login")

    fun getLoggedInUser(): Call<User>

    @GET("rest/users")
    fun getAllUsers(): Call<List<User>>

    @GET("rest/users/{id}")
    fun getUserById(@Path(value="id") id : String?): Call<User>


    @GET("rest/post/{id}")
    fun getPostById(@Path(value="id") id : String?): Call<Post>

    @GET("api/getUserFollowingPost/{id}")
    fun getFollowingsPost(@Path(value="id") id : String?):Call<List<Post>>


}