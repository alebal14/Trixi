package com.example.trixi.entities


data class Category(
    val uid: String,
    val name: String
){

    override fun toString(): String {
        return name
    }

}