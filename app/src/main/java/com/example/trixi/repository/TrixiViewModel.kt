package com.example.trixi.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Pet
import com.example.trixi.entities.Post
import com.example.trixi.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrixiViewModel : ViewModel() {

    private val TAG = "TrixiViewModel"
    private val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

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

    fun getFollowingsPosts(id: String): MutableLiveData<List<Post>?> {
        val followingsPosts: MutableLiveData<List<Post>?> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("home", "getting followings post")
            val fPosts = retrofitClient?.getFollowingsPost(id)?.body()
            followingsPosts.postValue(fPosts)
            Log.d("home", "got followings post")
        }
        return followingsPosts

    }

    fun getAllPosts()  {
        val allPosts: MutableLiveData<List<Post>?> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All post")
            val p = retrofitClient?.getAllPosts()?.body()
            allPosts.postValue(p)
        }

    }

    fun getOneUser(id: String): MutableLiveData<User>? {
        val userById: MutableLiveData<User>? = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "getting one User")
                val aUser = retrofitClient?.getUserById(id)?.body()
                userById?.postValue(aUser)
            } catch (e: Exception) {
                userById?.postValue(null)
                // Handle exception
            }
        }
        return userById
    }

    fun getOnePet(id: String): MutableLiveData<Pet>? {
        val petById: MutableLiveData<Pet>? = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "getting one Pet")
                val aPet = retrofitClient?.getPetById(id)?.body()
                petById?.postValue(aPet)

            } catch (e: Exception) {
                petById?.postValue(null)
            }
        }
        return petById
    }

    fun getPostsByOwner(id: String): MutableLiveData<List<Post>>? {
        val postsByOwner: MutableLiveData<List<Post>>? = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting posts by owner")
            val posts = retrofitClient?.getPostByOwnerId(id)?.body()
            //postsByOwner as MutableLiveData
            postsByOwner?.postValue(posts)
        }
        return postsByOwner
    }

    fun getPetsByOwner(id: String): MutableLiveData<List<Pet>>? {
        val petsByOwner: MutableLiveData<List<Pet>>? = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting pets by owner")
            val pets = retrofitClient?.getPetsByOwnerId(id)?.body()
            petsByOwner?.postValue(pets)
        }
        return petsByOwner
    }

}