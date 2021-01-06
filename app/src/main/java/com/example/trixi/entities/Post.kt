package com.example.trixi.entities;

import java.io.Serializable


data class Post(
    val uid: String?,
    val title: String?,
    val description: String?,
    val filePath: String?,
    val fileType: String?,
    val ownerId: String?,
    val categoryName: String?,
    var comments: List<Comment>?,
    var likes: List<Like>?,
    var owner: User? = null,
    var ownerIsPet :Pet? = null
): Serializable {
}

data class FollowingsPosts(
    val isSuccessfull: Boolean,
    val followingsPost: List<Post>
) {

}


