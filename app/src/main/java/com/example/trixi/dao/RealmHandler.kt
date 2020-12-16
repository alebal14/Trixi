package com.example.trixi.dao

import android.graphics.ColorSpace.Model
import android.util.Log
import android.view.Gravity.apply
import androidx.core.view.GravityCompat.apply
import com.example.trixi.apiService.Api
import com.example.trixi.apiService.RetrofitClient
import com.example.trixi.entities.Pet
import com.example.trixi.entities.RealmUserEntity
import com.example.trixi.entities.User
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RealmHandler(realm: Realm){

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


    private fun saveAllUsersToRealm(user: List<User>) {
        //realm.deleteAll()
        realm.executeTransactionAsync(fun(realm: Realm) {
            user.forEach { u ->
                /* val usersFromDb = realm.where(RealmUserEntity::class.java)
                    .equalTo("uid", u.uid)
                    .findFirst()*/

                val users = RealmUserEntity().apply {
                    //realm = respones.body
                    uid = u.uid
                    userName = u.userName
                    email = u.email
                    bio = u.bio
                    imageUrl = u.imageUrl
                    role = u.role

                  /*  u.pets.forEach{ pet ->

                    }

                    pets!!.addAll(u.pets!!.map {
                        RealmUserEntity.apply {
                            type = it.type
                            url = it.url
                        })
                    pets = u.pets
                    posts = u.posts*/
                }
                realm.insertOrUpdate(users)
            }
        }, fun() {
            Log.d("realm", "user uploaded to realm")
        })
    }
}