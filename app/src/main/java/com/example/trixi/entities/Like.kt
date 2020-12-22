package com.example.trixi.entities

data class Like(val postId: String,
                val userId:String,
                var owner:User
)
