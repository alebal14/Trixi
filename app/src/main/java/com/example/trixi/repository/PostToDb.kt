package com.example.trixi.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.trixi.MainActivity
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.apiService.RetrofitClient.Companion.context
import com.example.trixi.entities.*
import com.example.trixi.ui.login.LoginActivity
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostToDb {

    companion object {
        var loggedInUser: User? = null
        var postedPost : Post? = null
        var createdPet : Pet? = null
    }



    fun PostLoginUserToDb(user: User, context: Context){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.loginUser(user)
        call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("user", "login-user : onfailure: " + t.message)
                Log.d("user", "login-user : onfailure: Username/email does not exist")
                Toast.makeText(context, "Username/email does not exist", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if (response.isSuccessful) {
                    Log.d("loggedInUser", "User logged in successfully")
                    loggedInUser = response.body()
                    Log.d("loggedInUser", loggedInUser.toString())

                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                } else {
                    Log.d(
                        "user",
                        "login-user : onResponse else: password and username/email dont match"
                    )
                    Toast.makeText(
                        context,
                        "Password and username/email dont match",
                        Toast.LENGTH_LONG
                    ).show()
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
                    Log.d("loggedInUser", "User logged in successfully")
                    loggedInUser = response.body()
                    Log.d("loggedInUser", loggedInUser.toString())

                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                } else {
                    Log.d("loggedInUser", "User failed to login")
                }
            }
        })
    }


    fun PostImageToDb(image: MultipartBody.Part) {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.postProfileImage(image)

        call?.enqueue(object : Callback<Image> {
            override fun onFailure(call: Call<Image>, t: Throwable) {
                //Log.d("user", "User: onfailure " + t.message)
            }

            override fun onResponse(
                call: Call<Image>, response: Response<Image>
            ) {
                if (response.isSuccessful) {
                    Log.d("imageurl", "Url:" + response.body()!!.url)
                    //PostRegisterUserToDb( response.body()!!.url)
                } else {
                    //Log.d("user", "User : onResponse else, failed" + response.message())
                }
            }
        })
    }

   /* fun PostImageToDb(image: String, user: User, context: Context) {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.postProfileImage(image)

        call?.enqueue(object : Callback<Image> {
            override fun onFailure(call: Call<Image>, t: Throwable) {
                //Log.d("user", "User: onfailure " + t.message)
            }
            override fun onResponse(
                    call: Call<Image>, response: Response<Image>
            ) {
                if(response.isSuccessful){
                    //Log.d("user", "User : onResponse success" + response.message())
                    PostRegisterUserToDb( response.body()!!.url , user, context)
                }else{
                    //Log.d("user", "User : onResponse else, failed" + response.message())
                }
            }
        })
    }*/

   fun logOutUser(context: Context?) {
       val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

       val call = retrofitClient?.logOutUser()
       call?.enqueue(object : Callback<ResponseBody> {
           override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
               Log.d("logout", "logout user : onfailure " + t.message)
           }

           override fun onResponse(
               call: Call<ResponseBody>, response: Response<ResponseBody>
           ) {
               if (response.isSuccessful) {
                   Log.d("logout", "User logged out successfully" + response.message())
                   val intent = Intent(context, LoginActivity::class.java)
                   context!!.startActivity(intent)

               } else {
                   Log.d("logout", "User failed to logout")
               }
           }
       })
   }

     fun PostRegisterUserToDb(
         image: MultipartBody.Part,
         userName: String,
         email: String,
         password: String,
         context: Context
     ){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.createUser(image, userName, email, password)
     call?.enqueue(object : Callback<User> {
         override fun onFailure(call: Call<User>, t: Throwable) {
             Log.d("uus", "REG-user : onfailure " + t.message)
         }

         override fun onResponse(
             call: Call<User>, response: Response<User>
         ) {
             if (response.isSuccessful) {
                 Log.d("uus", "REGuser : onResponse success" + response.body())
                 loggedInUser = response.body()
                 Log.d("loggedInUser", loggedInUser.toString())

                 val intent = Intent(context, MainActivity::class.java)
                 context.startActivity(intent)
             } else {
                 Log.d("uus", "REGuser : onResponse else" + response.body())

             }
         }
     })
    }

    fun sendPostToDb(
        image: MultipartBody.Part,
        fileType: String,
        description: String,
        ownerId: String,
        title: String,
        categoryName: String
    ) {

        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.postPostToDb(
            image,
            fileType,
            description,
            ownerId,
            title,
            categoryName
        )


        call?.enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d("post", "Post : onfailure " + t.message)

            }

            override fun onResponse(
                call: Call<Post>, response: Response<Post>
            ) {
                if (response.isSuccessful) {
                    postedPost = response.body()
                    Log.d("post", "Post : onResponse success" + (response.body()!!.uid))

                    if (postedPost != null) {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }

                } else {
                    Log.d("post", "Post : onResponse else" + response.body()!!.uid)
                }
            }

        })

    }

    fun sendPetToDb(
        image: MultipartBody.Part,
        ownerId: String,
        name: String,
        age: String,
        bio: String,
        breed: String,
        petType: String,
        gender: String
    ) {

        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

        val call = retrofitClient?.postPet(image, ownerId, name, age, bio, breed, petType, gender)


        call?.enqueue(object : Callback<Pet> {
            override fun onFailure(call: Call<Pet>, t: Throwable) {
                Log.d("pet", "Pet : onfailure " + t.message)

            }

            override fun onResponse(
                call: Call<Pet>, response: Response<Pet>
            ) {
                if (response.isSuccessful) {
                    createdPet = response.body()
                    Log.d("pet", "Pet : onResponse success" + (response.body()!!.uid))

                    if (createdPet != null) {
                        val intent = Intent(context, LoginActivity::class.java)
                        context!!.startActivity(intent)
                    }

                } else {
                    Log.d("pet", "Pet : onResponse else" + response.body()!!.uid)
                }
            }

        })

    }

    fun comment(comment: Comment){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        val call = retrofitClient?.commentAPost(comment)
        call?.enqueue(object : Callback<Comment> {
            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.d("comment", "comment : onFailure " + t.message)

            }

            override fun onResponse(
                call: Call<Comment>, response: Response<Comment>
            ) {
                if (response.isSuccessful) {
                    val c: Comment? = response.body()
                    Log.d("comment", c.toString())

                } else {
                    Log.d("comment", "fail to comment")
                }
            }
        })


    }


    fun like(like: Like){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        val call = retrofitClient?.likeAPost(like)
        call?.enqueue(object : Callback<Like> {
            override fun onFailure(call: Call<Like>, t: Throwable) {
                Log.d("like", "like : onFailure " + t.message)
            }

            override fun onResponse(
                call: Call<Like>, response: Response<Like>
            ) {
                if (response.isSuccessful) {
                    val l: Like? = response.body()
                    Log.d("like", l.toString())

                } else {
                    Log.d("like", "fail to like")
                }
            }
        })
    }


    fun unlike(like: Like){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        val call = retrofitClient?.unlikeAPost(like)
        call?.enqueue(object : Callback<Like> {
            override fun onFailure(call: Call<Like>, t: Throwable) {
                Log.d("like", "unlike : onFailure " + t.message)
            }

            override fun onResponse(
                call: Call<Like>, response: Response<Like>
            ) {
                if (response.isSuccessful) {
                    val l: Like? = response.body()
                    Log.d("like", l.toString())

                } else {
                    Log.d("like", "fail to unlike")
                }
            }
        })
    }

    fun follow(userId: String, followingUserId: String){
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        val call = retrofitClient?.postFollow(userId, followingUserId)

        call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("Follow", "follow : onFailure " + t.message)
            }

            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if (response.isSuccessful) {
                    val l: User? = response.body()
                    Log.d("Follow", l.toString())

                } else {
                    Log.d("Follow", "fail to follow")
                }
            }
        })
    }


    fun unfollow(userId: String?, followingUserId: String){

        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        val call = retrofitClient?.postUnfollow(userId, followingUserId)

        call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("Follow", "unfollow : onFailure " + t.message)
            }

            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if (response.isSuccessful) {
                    val l: User? = response.body()
                    Log.d("Unfollow", l.toString())

                } else {
                    Log.d("Unfollow", "fail to unfollow")
                }
            }
        })

    }




}