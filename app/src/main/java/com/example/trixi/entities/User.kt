package com.example.trixi.entities

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class User(
    val uid: String?,
    val userName: String?,
    val email: String?,
    val password: String?,
    val bio: String?,
    val imageUrl: String?,
    val role: String?,
    var pets: ArrayList<Pet>?,
    var posts: ArrayList<Post>?,
    val followingsUser: ArrayList<User>?,
    val followingsPet: ArrayList<Pet>?,
    val followers: ArrayList<User>?,
){
}

