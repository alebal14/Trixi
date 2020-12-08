@file:JvmName("ApiKt")
package com.example.trixi.apiService

import com.example.trixi.entities.User

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

   /* @FormUrlEncoded
    @POST("userlogin")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ): Call<LoginResponse>*/

    @GET("users")
    fun getAllUsers(): Call<List<User>>
}