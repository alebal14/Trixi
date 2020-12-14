package com.example.trixi.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.User
import com.example.trixi.ui.register.RegisterActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostToDb {

    companion object {
        var loggedInUser: User? = null
    }


    fun PostRegisterUserToDb(user: User){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.createUser(user)
     call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("uus", "REG-user : onfailure " + t.message)

            }
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if(response.isSuccessful){
                    Log.d("uus", "REGuser : onResponse success" + response.body())
                }else{
                    Log.d("uus", "REGuser : onResponse else" + response.body())
                }
            }
        })
    }

    fun PostLoginUserToDb(user: User, context: Context){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.loginUser(user)
        call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("uus", "login-user : onfailure: " + t.message)
                Log.d("uus", "login-user : onfailure: Username/email does not exist")
                Toast.makeText(context, "Username/email does not exist", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if(response.isSuccessful){
                    Log.d("uus", "login-user : onResponse success" + response.message())
                    GetLoggedInUserFromDB(context)
                }else{
                    Log.d("uus", "login-user : onResponse else: password and username/email dont match")
                    Toast.makeText(context, "Password and username/email dont match", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    fun GetLoggedInUserFromDB(context: Context){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.getLoggedInUser()
        call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("uus", "loggedInUser : onfailure " + t.message)
            }
            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if (response.isSuccessful) {
                    Log.d("loggedInUser", "success")
                    loggedInUser = response.body()
                    Log.d("loggedInUser", loggedInUser.toString())

                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)

                } else {

                }
            }
        })
    }

    fun PostImageToServer(image: String) {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.uploadProfileImage(image)

        call?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Image", "Image : onfailure " + t.message)
            }
            override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if(response.isSuccessful){
                    Log.d("Image", "Image : onResponse success" + response.message())
                }else{
                    Log.d("Image", "Image : onResponse else" + response.message())
                }
            }
        })
    }
//    fun postPostToDb(post: Post) {
//
//
//
//      //  val file = File(file.getPath())
//
////        val fbody: RequestBody = RequestBody.create(
////            MediaType.parse("image/*"),
////            file
////        )
////
////        RequestBody title = RequestBody.create(
////            MediaType.parse("text/plain"),
////            post.title.getText()
////                .toString()
////        )
////
////        val description: RequestBody = RequestBody.create(
////            MediaType.parse("text/plain"),
////            dessciption.getText()
////                .toString()
////        )
////
////        val ownerId: RequestBody = RequestBody.create(
////            MediaType.parse("text/plain"),
////            ownerId_field.getText()
////                .toString()
////        )
////
////        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
////
////
////        val call: Call<Post>? = retrofitClient?.postPost(
////            file,
////            title,
////            description,
////            ownerId);
//
////        call?.enqueue(object : Callback<Post> {
////            override fun onFailure(call: Call<Post>, t: Throwable) {
////                Log.d("post", "Post : onfailure " + t.message)
////
////            }
////            override fun onResponse(
////                call: Call<Post>, response: Response<Post>
////            ) {
////                if(response.isSuccessful){
////                    Log.d("post", "Post : onResponse success" + response.body())
////                }else{
////                    Log.d("post", "Post : onResponse else" + response.body())
////                }
////            }
//
//        })

   // }

}