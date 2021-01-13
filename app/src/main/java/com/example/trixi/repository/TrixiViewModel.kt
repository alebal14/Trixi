package com.example.trixi.repository

import android.util.Log
import androidx.lifecycle.*
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TrixiViewModel : ViewModel() {

    private val TAG = "TrixiViewModel"
    private val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)

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


    fun getAllPosts(): MutableLiveData<List<Post>?> {
        val allPosts: MutableLiveData<List<Post>?> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All post")
            val p = retrofitClient?.getAllPosts()?.body()
            allPosts.postValue(p)
        }
        return allPosts
    }

    fun getAllPostsWithQuery(page: Int, limit: Int) : MutableLiveData<List<Post>?> {
        val allPostsQuery: MutableLiveData<List<Post>?> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All post")
            val p = retrofitClient?.getAllPostQuery(page, limit)?.body()
            allPostsQuery.postValue(p)
        }
        return allPostsQuery
    }

    fun getPostBySearching(newText: String): MutableLiveData<List<Post>?> {
        val searchPosts: MutableLiveData<List<Post>?> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("explorer", "getting post by search")
            val fPosts = retrofitClient?.getPostBySearch(newText)?.body()
            searchPosts.postValue(fPosts)
            Log.d("explorer", "got post by search")
        }
        return searchPosts

    }

    fun getPostByType(petType: String): MutableLiveData<List<Post>?> {
        val petTypePosts: MutableLiveData<List<Post>?> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("explorer", "getting post by search")
            val fPosts = retrofitClient?.getPostByPetType(petType)?.body()
            petTypePosts.postValue(fPosts)
            Log.d("explorer", "got post by search")
        }
        return petTypePosts
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
        val allCategory = MutableLiveData<List<Category>>()
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All category")
            val categories = retrofitClient?.getAllCategories()?.body()
            allCategory as MutableLiveData
            allCategory.postValue(categories)
        }
        return allCategory
    }

    fun aPostById(id: String): MutableLiveData<Post> {
        val post = MutableLiveData<Post>()
        viewModelScope.launch(Dispatchers.IO) {
            val aPost = retrofitClient?.getPostById(id)?.body()
            post.postValue(aPost)
        }
        return post
    }


    fun getPetType(): MutableLiveData<List<PetType>> {
        val allPetType = MutableLiveData<List<PetType>>()
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "getting All Pet Type")
            val pettypes = retrofitClient?.getAllPetTypes()?.body()
            allPetType as MutableLiveData
            allPetType.postValue(pettypes)
        }
        return allPetType
    }

    fun getDiscoverPosts(id: String): MutableLiveData<List<Post>?> {
        val discoverPosts: MutableLiveData<List<Post>?> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("home", "getting followings post")
            val dPosts = retrofitClient?.getDiscover(id)?.body()
            discoverPosts.postValue(dPosts)
            Log.d("home", "got followings post")
        }
        return discoverPosts

    }

    fun getActivityByOwner(id: String): MutableLiveData<List<Activity>> {
        val activities: MutableLiveData<List<Activity>> = MutableLiveData()
        viewModelScope.launch(Dispatchers.IO) {
            val actFromDb = retrofitClient?.getNotifications(id)?.body()
            activities.postValue(actFromDb)
        }
        return activities
    }


}