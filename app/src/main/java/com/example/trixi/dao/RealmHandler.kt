package com.example.trixi.dao

import android.graphics.ColorSpace.Model
import android.util.Log
import android.view.Gravity.apply
import androidx.core.view.GravityCompat.apply
import androidx.lifecycle.MutableLiveData
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.*
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RealmHandler(realm: Realm) {

    val service = RetrofitClient.getRetroInstance()?.create(Api::class.java)

    companion object {
        val realm = Realm.getDefaultInstance()
    }

    fun getAllUsersFromDB() {

        val call = service?.getAllUsers()
        call?.enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("uus", "users : onfailure " + t.message)
            }

            override fun onResponse(
                    call: Call<List<User>>, response: Response<List<User>>
            ) {
                if (response.isSuccessful) {
                    saveAllUsersToRealm(response.body()!!)
                } else {

                }
            }
        })
        return
    }


    private fun saveAllUsersToRealm(users: List<User>) {
        //realm.deleteAll()
        realm.executeTransactionAsync(fun(realm: Realm) {
            users.forEach { u ->
                /* val usersFromDb = realm.where(RealmUserEntity::class.java)
                    .equalTo("uid", u.uid)
                    .findFirst()*/

                val user = RealmUser().apply {
                    //realm = respones.body
                    uid = u.uid
                    userName = u.userName
                    email = u.email
                    bio = u.bio
                    imageUrl = u.imageUrl
                    role = u.role

                    pets?.addAll(u.pets!!.map {
                        RealmPet().apply {
                            uid = it.uid
                            ownerId = it.ownerId
                            name = it.name
                            imageUrl = it.imageUrl
                            age = it.age
                            bio = it.bio
                            breed = it.breed
                            gender = it.gender

                        }
                    })

                    posts?.addAll(u.posts!!.map {
                        RealmPost().apply {
                            uid = it.uid
                            title = it.title
                            description = it.description
                            filePath = it.filePath
                            ownerId = it.ownerId
                            comments?.addAll(it.comments!!.map {
                                RealmComment().apply {
                                    comment = it.comment
                                    userId = it.userId
                                    postId = it.postId
                                }
                            })
                            likes?.addAll(it.likes!!.map {
                                RealmLike().apply {
                                    userId = it.userId
                                    postId = it.postId
                                }
                            })
                        }
                    })

                    followers?.addAll(u.followers!!.map {
                        RealmUser().apply {
                            uid = it.uid
                            userName = it.userName
                        }
                    })
                    followingsUser?.addAll(u.followingsUser!!.map {
                        RealmUser().apply {
                            uid = it.uid
                            userName = it.userName
                        }
                    })
                    followingsPet?.addAll(u.followingsPet!!.map {
                        RealmPet().apply {
                            uid = it.uid
                            name = it.name
                        }
                    })
                }
                realm.insertOrUpdate(user)
            }
        }, fun() {
            Log.d("realm", "user uploaded to realm")
        })
    }

    fun getFollowingsPostsFromDb(id: String?) {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        //var followingsPost: MutableLiveData<List<Post>> = MutableLiveData();
        val call = retrofitClient?.getFollowingsPost(id)
        call?.enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d("posts", "posts : onfailure " + t.message)
            }

            override fun onResponse(
                    call: Call<List<Post>>, response: Response<List<Post>>
            ) {
                if (response.isSuccessful) {
                    savePostList(response.body()!!)
                } else {

                }
            }
        })
        return

    }

    private fun savePostList(posts: List<Post>) {
        realm.executeTransactionAsync(fun(realm: Realm) {
            posts.forEach { p ->

                val user = RealmPost().apply {
                    //realm = respones.body
                    uid = p.uid
                    title = p.title
                    description = p.description
                    filePath = p.filePath
                    ownerId = p.ownerId
                    comments?.addAll(p.comments!!.map {
                        RealmComment().apply {
                            comment = it.comment
                            userId = it.userId
                            postId = it.postId
                        }
                    })
                    likes?.addAll(p.likes!!.map {
                        RealmLike().apply {
                            userId = it.userId
                            postId = it.postId
                        }
                    })
                }
                realm.insertOrUpdate(user)
            }
        }, fun() {
            Log.d("realm", "followingsposts uploaded to realm")
        })
    }


}