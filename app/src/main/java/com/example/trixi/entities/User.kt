package com.example.trixi.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class User(
    val uid: String?,
    val userName: String?,
    val email: String?,
    val password: String?,
    val bio: String?,
    val imageUrl: String?,
    val role: String?,
    val pets: ArrayList<Pet>?,
    val posts: ArrayList<Post>?,
    val followingsUser: ArrayList<User>?,
    val followingsPet: ArrayList<Pet>?,
    val followers: ArrayList<User>?,
)

