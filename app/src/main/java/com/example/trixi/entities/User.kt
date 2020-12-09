package com.example.trixi.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.bson.types.ObjectId

data class User ( val uid: String?, val userName: String?, val email: String?, val password: String?, val bio: String?, val imageUrl: String?, val role: String?, val pets: ArrayList<Pet>?,
                  val posts: ArrayList<Post>?, val type : String = "user") {




}


