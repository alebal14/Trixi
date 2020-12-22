package com.example.trixi.entities

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class User(
    val uid: String?,
    val userName: String?,
    val email: String?,
    val password: String?,
    val bio: String?,
    val imageUrl: String?,
    val role: String?,
    val pets: ArrayList<Pet>?,
    val posts: ArrayList<Post>?,
    val followingsUser: ArrayList<User>?,
    val followingsPet: ArrayList<Pet>?,
    val followers: ArrayList<User>?,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("pets"),
        TODO("posts"),
        TODO("followingsUser"),
        TODO("followingsPet"),
        TODO("followers")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(userName)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(bio)
        parcel.writeString(imageUrl)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

