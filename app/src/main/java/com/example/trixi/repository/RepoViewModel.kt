package com.example.trixi.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoViewModel : ViewModel() {

    lateinit var userListData: MutableLiveData<List<User>>

    init {
        userListData = MutableLiveData()
    }

    fun getUserMutableLiveDataList(): MutableLiveData<List<User>> {
        return userListData
    }

    fun makeApiCall(){
        val retrofitClient = RetrofitClient.getRetroInstance().create(Api::class.java)
        val call = retrofitClient.getAllUsers()
        call.enqueue(object : Callback<List<User>>{
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("uus", "users : onfailure " + t.message)
                userListData.postValue(null)

            }
            override fun onResponse(
                call: Call<List<User>>, response: Response<List<User>>) {
                if(response.isSuccessful){
                    userListData.postValue(response.body())
                }else{
                    userListData.postValue(null)
                }
            }
        })










    }
}