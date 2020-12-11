package com.example.trixi.entities

data class Post(val uid: String?,
                val title:String?,
                val description:String?,
                val filePath: String?,
                val ownerId:String?,
                val categoryId: String?,
                val comments: List<Comment>?,
                val likes:List<Like>?

){
}



