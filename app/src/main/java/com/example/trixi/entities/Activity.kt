package com.example.trixi.entities

class Activity(
    val uid:String,
    val postOwnerId:String,
    val comment:Comment?,
    val like:Like?,
    val post:Post
) {
}