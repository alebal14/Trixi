@file:JvmName("ApiKt")
package com.example.trixi.apiService

import com.example.trixi.entities.ProfileImage
import com.example.trixi.entities.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("users")
    fun createUser(@Body user: User): Call<User>

   @Multipart
    @POST("users")
    fun uploadProfileImage(
     //  @Part ("name") name :RequestBody,
        @Part file: MultipartBody.Part
    ):Call<ResponseBody>

    //@Part files: MultipartBody.Part

    //@FormUrlEncoded
    //@POST("")
    //fun uploadProfileImage(@Body ): Call<>

    @POST("login")
    fun loginUser(@Body user: User): Call<User>

    @GET("login")
    fun getLoggedInUser(): Call<User>

    @GET("users")
    fun getAllUsers(): Call<List<User>>
}