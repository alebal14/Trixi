package com.example.trixi.entities

import java.io.Serializable

data class Post (
                val _id: String,
                val commentIds: ArrayList<String>?,
                val comments: ArrayList<String>?,
                val description: String?,
                val filePath: String?,
                val likes: ArrayList<String>?,
                val ownerId: String,
                val title: String
) : Serializable

