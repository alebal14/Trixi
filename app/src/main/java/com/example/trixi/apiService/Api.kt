@file:JvmName("ApiKt")
package com.example.trixi.apiService

import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @POST("users")
    fun createUser(@Body user: User): Call<User>

    @POST("login")
    fun loginUser(@Body user: User): Call<User>

    @Multipart
    @POST("users")
    fun uploadProfileImage(@Part("file") file: String,):Call<ResponseBody>


    @Multipart
    @POST("posts")
    fun postPicToDb(@Part("file") file: String,):Call<ResponseBody>

    @POST("posts")
    fun postPostToDb(@Body post: Post) : Call<Post>?

    @GET("login")
    fun getLoggedInUser(): Call<User>

    @GET("users")
    fun getAllUsers(): Call<List<User>>
}