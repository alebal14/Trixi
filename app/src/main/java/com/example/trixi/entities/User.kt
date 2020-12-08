package com.example.trixi.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.bson.types.ObjectId

data class User ( val id: ObjectId, val userName: String) {

}

