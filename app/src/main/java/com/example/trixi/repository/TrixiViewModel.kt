package com.example.trixi.repository

import android.util.Log
import android.widget.MultiAutoCompleteTextView
import androidx.lifecycle.*
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.*
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

    fun getAllUsers(): MutableLiveData<List<User>?> {
        val allUsers: MutableLiveData<List<User>?> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All users")
            val p = retrofitClient?.getAllUsers()?.body()
            allUsers.postValue(p)
        }
        return allUsers
    }

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


    fun getAllPosts() : MutableLiveData<List<Post>?> {
        val allPosts: MutableLiveData<List<Post>?> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All post")
            val p = retrofitClient?.getAllPosts()?.body()
            allPosts.postValue(p)
        }
        return allPosts
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

    fun getAllCategories(): MutableLiveData<List<Category>> {
        val allCategory =  MutableLiveData<List<Category>>()
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All category")
            val categories = retrofitClient?.getAllCategories()?.body()
            allCategory as MutableLiveData
            allCategory.postValue(categories)
        }
        return allCategory
    }

    fun aPostById(id: String):MutableLiveData<Post>{
        val post = MutableLiveData<Post>()
        viewModelScope.launch(Dispatchers.IO){
            val aPost = retrofitClient?.getPostById(id)?.body()
            post.postValue(aPost)
        }
        return post
    }


    fun getPetType(): MutableLiveData<List<PetType>> {
        val allPetType =  MutableLiveData<List<PetType>>()
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All Pet Type")
            val pettypes = retrofitClient?.getAllPetTypes()?.body()
            allPetType as MutableLiveData
            allPetType.postValue(pettypes)
        }
        return allPetType
    }

}