@file:JvmName("ApiKt")
package com.example.trixi.apiService

import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File


interface Api {

    @POST("users")
    fun createUser(@Body user: User): Call<User>

    @POST("login")
    fun loginUser(@Body user: User): Call<User>

    @Multipart
    @POST("users")
    fun uploadProfileImage(
            @Part("file") file: File,
            //@Part("name") filename: String,
            //@Part files: MultipartBody.Part
    ):Call<ResponseBody>


    @Multipart
    @POST("post")
    fun postPost(
        //@Part("file") file: Files,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("ownerId") ownerId: RequestBody?,
    ) : Call<Post>?

    @GET("login")
    fun getLoggedInUser(): Call<User>

    @GET("users")
    fun getAllUsers(): Call<List<User>>
}