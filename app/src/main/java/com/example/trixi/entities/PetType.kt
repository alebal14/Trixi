package com.example.trixi.entities

data class PetType(
    val uid: String,
    val iconUrl: String,
    val name: String
){

    override fun toString(): String {
        return name
    }

}