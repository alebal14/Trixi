package com.example.trixi.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
//import com.example.trixi.dao.RealmHandler
import com.example.trixi.dao.asLiveData
import com.example.trixi.entities.*
import com.example.trixi.entities.RealmPost
import com.example.trixi.entities.RealmUser
import com.example.trixi.entities.User
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.Continuation

class DataViewModel : ViewModel() {

    // TODO: 2020-12-16  get All Post, get all Post by ownerId, get posts by category ID
    // TODO: get all category, get all pet types
    // TODO: 2020-12-16 get all pets, get pets by pet Types, get pet by pet_id , get pet by owner_id

//    val realm: Realm by lazy {
//        Realm.getDefaultInstance()
//    }
//
//    val api = RealmHandler(realm)
//
//
//    val getAllUsersResults: LiveData<RealmResults<RealmUser>> by lazy {
//        realm.where(RealmUser::class.java).findAllAsync().asLiveData()
//    }
//
//    fun getAllUsersData(): LiveData<RealmResults<RealmUser>> {
//        api.getAllUsersFromDB()
//        return getAllUsersResults
//    }


//    val getFollowingsPostsResults: LiveData<RealmResults<RealmFollowingPost>> by lazy {
//        realm.where(RealmFollowingPost::class.java).findAllAsync().asLiveData()
//
//    }
//
//    fun getFollowingsPostsData(uid: String): LiveData<RealmResults<RealmFollowingPost>> {
//        api.getFollowingsPostsFromDb(uid)
//        return getFollowingsPostsResults
//    }


//    val getUserPostsResults: LiveData<RealmResults<RealmUserPost>> by lazy {
//        realm.where(RealmUserPost::class.java).findAllAsync().asLiveData()
//    }
//
//    fun getUserPostsData(uid: String): LiveData<RealmResults<RealmUserPost>> {
//        api.getUserPostsFromDb(uid)
//        return getUserPostsResults
//    }

//    val getAllPostsResults: LiveData<RealmResults<RealmPost>> by lazy {
//
//        realm.where(RealmPost::class.java).findAllAsync().asLiveData()
//    }
//
//    fun getAllPostsData(): LiveData<RealmResults<RealmPost>> {
//        api.getALLPostsFromDb()
//        return getAllPostsResults
//    }
//
//
//    fun findUserFromRealmById(uid: String): LiveData<RealmResults<RealmUser>> {
//        return realm.where(RealmUser::class.java)
//            .equalTo("uid", uid)
//            .findAllAsync().asLiveData()
//    }


//    val getAllPetsResults: LiveData<RealmResults<RealmPet>> by lazy {
//        realm.where(RealmPet::class.java).findAllAsync().asLiveData()
//    }
//
//    fun getAllPetsData(): LiveData<RealmResults<RealmPet>> {
//        api.getAllPetsFromDB()
//        return getAllPetsResults
//    }

//    val getPetsByOwnerResults: LiveData<RealmResults<RealmPetByOwner>> by lazy {
//        realm.where(RealmPetByOwner::class.java).findAllAsync().asLiveData()
//    }
//
//    fun getPetsByOwnerData(uid: String): LiveData<RealmResults<RealmPetByOwner>> {
//        api.getPetsByOwnerFromDb(uid)
//        return getPetsByOwnerResults
//
//    }


    /*fun getAllUsersFromDB(): MutableLiveData<List<User>> {
    val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
    var userListData: MutableLiveData<List<User>> = MutableLiveData()
    val call = retrofitClient?.getAllUsers()
    call?.enqueue(object : Callback<List<User>> {
        override fun onFailure(call: Call<List<User>>, t: Throwable) {
            Log.d("uus", "users : onfailure " + t.message)
            userListData.postValue(null)
        }

        override fun onResponse(
            call: Call<List<User>>, response: Response<List<User>>
        ) {
            if (response.isSuccessful) {
                userListData.postValue(response.body())
            } else {
                userListData.postValue(null)
            }
        }
    })
    return userListData
}*/


//    fun getFollowingsPostFromDb(id: String?): MutableLiveData<List<Post>> {
//        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
//
//        var followingsPost: MutableLiveData<List<Post>> = MutableLiveData();
//        val call = retrofitClient?.getFollowingsPost(id)
//        call?.enqueue(object : Callback<List<Post>> {
//            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//                Log.d("post", "posts : onfailure " + t.message)
//                followingsPost.postValue(null)
//            }
//
//            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                if (response.isSuccessful) {
//                    followingsPost.postValue(response.body())
//
//                } else {
//                    followingsPost.postValue(null)
//                }
//            }
//        })
//        return followingsPost;
//
//
//    }
//
//
//    fun getOneUserFromDb(id: String?): MutableLiveData<User> {
//        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
//        val user: MutableLiveData<User> = MutableLiveData()
//        val call = retrofitClient?.getUserById(id)
//        call?.enqueue(object : Callback<User> {
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Log.d("uus", "user : onfailure " + t.message)
//                user.postValue(null)
//            }
//
//            override fun onResponse(
//                call: Call<User>, response: Response<User>
//            ) {
//                if (response.isSuccessful) {
//                    Log.d("uus", "success")
//                    user.postValue(response.body())
//                } else {
//                    user.postValue(null)
//                }
//
//            }
//
//        })
//        return user
//
//    }
}



