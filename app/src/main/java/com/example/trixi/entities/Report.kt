package com.example.trixi.entities

data class Report(
    val uid:String?,
    val reporter: User,
    val reportText: String,
    val post:Post
) {
}