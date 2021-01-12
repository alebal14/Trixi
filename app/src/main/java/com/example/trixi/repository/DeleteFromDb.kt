package com.example.trixi.repository

import android.content.Intent
import android.util.Log
import com.example.trixi.MainActivity
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.apiService.RetrofitClient.Companion.context
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class DeleteFromDb {
    companion object {
        var postDeleted = false
        var petDeleted = false
    }

    fun deleteAPostFromDb(postId: String) {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        val call = retrofitClient?.deleteAPost(postId)
        call?.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    var result = response.body()
                    Log.d("delete", "delete : successfully deleted:---- $result")
                    Log.d("delete", "delete: id :$postId")
                    postDeleted = true
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                } else {
                    Log.d("delete", "delete : failed to delete ")
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("delete", "delete : onFailure " + t.message)
            }
        })
    }

    fun deleteAPetFromDb(petId: String) {
        val retrofitClient = RetrofitClient.getRetroInstance()?.create(Api::class.java)
        val call = retrofitClient?.deleteAPet(petId)
        call?.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    var result = response.body()
                    Log.d("delete", "delete : successfully deleted:---- $result")
                    Log.d("delete", "delete: id :$petId")
                    petDeleted = true
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                } else {
                    Log.d("delete", "delete : failed to delete ")
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("delete", "delete : onFailure " + t.message)
            }
        })
    }
}