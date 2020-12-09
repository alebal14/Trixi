@file:JvmName("ApiKt")
package com.example.trixi.apiService

import com.example.trixi.entities.User

import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("users")
    fun createUser(@Body user: User): Call<User>

    @POST("login")
    fun loginUser(@Body user: User): Call<User>

    @GET("users")
    fun getAllUsers(): Call<List<User>>
}