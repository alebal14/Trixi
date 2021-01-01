@file:JvmName("ApiKt")

package com.example.trixi.apiService


import android.content.Context
import com.example.trixi.entities.*
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor


import retrofit2.Call
import retrofit2.http.*
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.*
import java.util.concurrent.TimeUnit

interface Api {

    //Login/Log Out
    @POST("rest/login")
    fun loginUser(@Body user: User): Call<User>

    @GET("rest/login")
    fun getLoggedInUser(): Call<User>

    @GET("rest/logout")
    fun logOutUser(): Call<ResponseBody>


    /* //Posting Images
     @Multipart
     @POST("rest/image")
     fun postProfileImage(
             @Part("file") file: String
     ):Call<Image>*/

    @Multipart
    @POST("rest/image")
    fun postProfileImage(
        @Part file: MultipartBody.Part
    ): Call<Image>

    @Multipart
    @POST("rest/users")
    fun createUser(
        @Part file: MultipartBody.Part,
        @Part("userName") userName: String,
        @Part("email") email: String,
        @Part("password") password: String
    ): Call<User>


    //Api User Collection
    /* @POST("rest/users")
     fun createUser(@Body user: User, @Query("imageUrl") url: String): Call<User>*/

    @GET("rest/users")
    suspend fun getAllUsers(): Response<List<User>>

    @GET("rest/users/{id}")
    suspend fun getUserById(@Path(value = "id") id: String?): Response<User>

    //Api Pet Collection
    @GET("rest/pets")
    suspend fun getAllPets(): Response<List<Pet>>

    @GET("rest/pets/{id}")
    suspend fun getPetById(@Path(value = "id") id: String?): Response<Pet>

    @GET("rest/pets/by_pet_type/{pet_type_id}")
    suspend fun getPetsByPetType(@Path(value = "pet_type_id") id: String?): Response<List<Pet>>

    //
    @GET("rest/pets/by_owner/{ownerid}")
    suspend fun getPetsByOwnerId(@Path("ownerid") id: String?): Response<List<Pet>>
//
//    @POST("rest/pets")
//    fun postPet(@Body pet: Pet): Call<Pet>
//
//
//    //Pet type
//    @GET("rest/pet_types")
//    fun getAllPetTypes(): Call<List<PetType>>


    //Posts
    @GET("rest/posts")
    suspend fun getAllPosts(): Response<List<Post>>


    @Multipart
    @POST("rest/posts")
    fun postPostToDb(
        @Part file: MultipartBody.Part,
        @Part("fileType") fileType: String,
        @Part("description") description: String,
        @Part("ownerId") ownerId: String,
        @Part("title") title: String,
        @Part("categoryName") categoryName: String
    ): Call<Post>

    @GET("rest/posts/{id}")
    suspend fun getPostById(@Path("id") id: String?): Response<Post>

    @GET("rest/posts/by_owner/{owner_id}")
    suspend fun getPostByOwnerId(@Path("owner_id") id: String?): Response<List<Post>>
//
//    @GET("rest/posts/by_category/{category_id}")
//    fun getPostByCategoryId(@Path(value="category_id") id : String?): Call<List<Post>>

    @GET("api/getUserFollowingPost/{id}")
    suspend fun getFollowingsPost(@Path("id") id: String?): Response<List<Post>>


    //Category
    @GET("rest/categories")
    suspend fun getAllCategories(): Response<List<Category>>


    @POST("rest/comments")
    fun commentAPost(@Body comment: Comment): Call<Comment>


    @POST("rest/delete_comment")
    fun deleteComment(@Body comment: Comment): Call<Comment>


    @POST("rest/likes")
    fun likeAPost(@Body like: Like): Call<Like>

    @POST("rest/unlike")
    fun unlikeAPost(@Body like: Like): Call<Like>


//    @GET("api/getLatestPost/{id}")
//    suspend fun getLatestPost(@Path(value = "id") id: String?): Response<Post>

}