package com.example.trixi.entities;

import io.realm.internal.Keep
import java.io.File


data class Post(
    val uid: String?,
    val title: String?,
    val description: String?,
    val filePath: String?,
    val ownerId: String?,
    var comments: List<Comment>?,
    var likes: List<Like>?,
    var owner: User? = null,
    var ownerIsPet :Pet? = null
) {
}

data class FollowingsPosts(
    val isSuccessfull: Boolean,
    val followingsPost: List<Post>
) {

}


