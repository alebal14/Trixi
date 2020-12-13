package com.example.trixi.entities;

import java.io.File


data class Post(val uid: String?, val title:String?, val description:String?, val filePath:String?, val ownerId:String?, val comments: ArrayList<Comment>?, val likes: ArrayList<Like>?) {
}
