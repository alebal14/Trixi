package com.example.trixi.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.FollowingsPosts
import com.example.trixi.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrixiViewModel : ViewModel() {

    private val TAG = "TrixiViewModel"
    val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)


    val followingsPosts: LiveData<List<Post>?> = MutableLiveData()
    val allPosts: LiveData<List<Post>?> = MutableLiveData()
    val postsByOwner: LiveData<List<Post>?> = MutableLiveData()
    val petsByOwner: LiveData<List<Pet>?> = MutableLiveData()



//    init {
//
//        viewModelScope.launch{
//            //allPosts as MutableLiveData
//            followingsPosts as MutableLiveData
//
////            allPosts.value = getAllPosts()
//            followingsPosts.value = getFollowingsPosts("5fd622cc746f5a50f0f10ed9")
//
//
//            //allPosts.value = async { getAllPosts() }.await()
//
//            //followingsPosts.value = async { getFollowingsPosts("5fd622cc746f5a50f0f10ed9") }.await()
//
//            //followingsPosts.value = PostToDb.loggedInUser?.uid?.let { getFollowingsPosts(it) }
//
//        }
//
//    }

    fun getFollowingsPosts(id: String) {

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("home", "getting followings post")
            val fPosts = retrofitClient?.getFollowingsPost(id)?.body()
            fPosts?.forEach {

            }
            followingsPosts as MutableLiveData
            followingsPosts.postValue(fPosts)
            Log.d("home", "got followings post")
        }

    }

    fun getAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All post")
            val p = retrofitClient?.getAllPosts()?.body()
            allPosts as MutableLiveData
            allPosts.postValue(p)
        }
    }

    fun getOneUser(id: String): MutableLiveData<User>? {
        val userById: MutableLiveData<User>? = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting one User post")
            val aUser = retrofitClient?.getUserById(id)?.body()
            userById?.postValue(aUser)

        }
        return userById
    }

    fun getPostsByOwner(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting posts by owner")
            val posts = retrofitClient?.getPostByOwnerId(id)?.body()
            postsByOwner as MutableLiveData
            postsByOwner.postValue(posts)


        }
    }

    fun getPetsByOwner(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting pets by owner")
            val pets = retrofitClient?.getPetsByOwnerId(id)?.body()
            petsByOwner as MutableLiveData
            petsByOwner.postValue(pets)


        }
    }
}