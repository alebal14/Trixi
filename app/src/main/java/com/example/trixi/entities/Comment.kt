
package com.example.trixi.entities

data class Comment(
    val comment:String,
    val postId: String,
    val userId:String,
    var owner: User?
)

