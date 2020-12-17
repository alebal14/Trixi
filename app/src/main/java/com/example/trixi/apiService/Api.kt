@file:JvmName("ApiKt")
package com.example.trixi.apiService


import com.example.trixi.entities.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

import okhttp3.ResponseBody



import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface Api {

    //Login/Log Out
    @POST("rest/login")
    fun loginUser(@Body user: User): Call<User>

    @GET("rest/login")
    fun getLoggedInUser(): Call<User>

    @GET("rest/logout")
    fun logOutUser():Call<ResponseBody>


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
    ):Call<Image>

    @Multipart
    @POST("rest/users")
    fun createUser(
        @Part file: MultipartBody.Part,
        @Part("userName") userName: String,
        @Part("email") email: String,
        @Part("password") password:String
    ):Call<User>


    //Api User Collection
   /* @POST("rest/users")
    fun createUser(@Body user: User, @Query("imageUrl") url: String): Call<User>*/

    @GET("rest/users")
    fun getAllUsers(): Call<List<User>>

    @GET("rest/users/{id}")
    fun getUserById(@Path(value="id") id : String?): Call<User>

    //Api Pet Collection
    @GET("rest/pets")
    fun getAllPets(): Call<List<Pet>>

    @GET("rest/pets/{id}")
    fun getPetById(@Path(value = "id") id: String?): Call<Pet>

    @GET("rest/pets/by_pet_type/{pet_type_id}")
    fun getPetsByPetType(@Path(value = "pet_type_id") id: String?): Call<List<Pet>>

    @GET("rest/pets/by_ownerId/{ownerid}")
    fun getPetsByOwnerId(@Path(value = "ownerid") id: String?): Call<List<Pet>>

    @POST("rest/pets")
    fun postPet(@Body pet: Pet): Call<Pet>


    //Pet type
    @GET("rest/pet_types")
    fun getAllPetTypes(): Call<List<PetType>>


    //Posts
    @GET("rest/posts")
    fun getAllPosts(): Call<List<Post>>

    @POST("rest/posts")
    fun postPostToDb(@Body post: Post) : Call<Post>

    @GET("rest/posts/{id}")
    fun getPostById(@Path(value="id") id : String?): Call<Post>

    @GET("rest/posts/by_owner/{owner_id}")
    fun getPostByOwnerId(@Path(value="owner_id") id : String?): Call<List<Post>>

    @GET("rest/posts/by_category/{category_id}")
    fun getPostByCategoryId(@Path(value="category_id") id : String?): Call<List<Post>>

    @GET("api/getUserFollowingPost/{id}")
    fun getFollowingsPost(@Path(value="id") id : String?):Call<List<Post>>


    //Category
   @GET("rest/categories")
   fun getAllCategories(): Call<List<Category>>













   // @GET("api/getLatestPost/{id}")
   // fun getLatestPost(@Path(value="id") id : String?):Call<Post>







}