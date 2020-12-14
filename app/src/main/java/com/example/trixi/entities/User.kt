package com.example.trixi.entities

import java.io.Serializable

data class User(
    val uid: String?,
    val userName: String?,
    val email: String?,
    val password: String?,
    val bio: String?,
    val imageUrl: String?,
    val role: String?,
    val pets: ArrayList<Pet>?,
    val posts: ArrayList<Post>?
) {


}


