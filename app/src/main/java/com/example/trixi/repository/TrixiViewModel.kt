package com.example.trixi.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.FollowingsPosts
import com.example.trixi.entities.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrixiViewModel : ViewModel() {

    private val TAG = "TrixiViewModel"
    val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)


    val followingsPosts: LiveData<List<Post>?> = MutableLiveData()
    val allPosts: LiveData<List<Post>?> = MutableLiveData()

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

    fun getFollowingsPosts(id: String){

        viewModelScope.launch (Dispatchers.Default) {
            Log.d(TAG, "getting followings post")
            val fPosts = retrofitClient?.getFollowingsPost(id)?.body()
            followingsPosts as MutableLiveData
            followingsPosts.postValue(fPosts)
        }

    }

    private suspend fun getAllPosts(): List<Post>? {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "getting All post")
            retrofitClient?.getAllPosts()?.body()
        }
    }
}