@file:JvmName("ApiKt")
package com.example.trixi.apiService

import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File


interface Api {

    @POST("users")
    fun createUser(@Body user: User): Call<User>

    @POST("login")
    fun loginUser(@Body user: User): Call<User>



    @Multipart
    @POST("post")
    fun postPost(
        @Part("file\"; filename=\"pp.png\" ") file: File,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("ownerId") ownerId: RequestBody?,
    ) : Call<Post>?

    @GET("login")
    fun getLoggedInUser(): Call<User>

    @GET("users")
    fun getAllUsers(): Call<List<User>>
}