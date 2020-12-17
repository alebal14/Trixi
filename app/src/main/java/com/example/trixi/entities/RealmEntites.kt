package com.example.trixi.entities

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.io.File

@RealmClass
open  class RealmUser: RealmObject(){
    @PrimaryKey
    //var id: Int? = null
    var uid: String? = null
    var userName: String? = null
    var email: String? = null
    var bio: String? = null
    var imageUrl: String? = null
    var role: String? = null
    var pets: RealmList<RealmPet>? = RealmList()
    var posts: RealmList<RealmPost>? = RealmList()
    val followingsUser: RealmList<RealmUser>? = RealmList()
    val followingsPet: RealmList<RealmPet>?= RealmList()
    val followers: RealmList<RealmUser>?= RealmList()

}


@RealmClass
open  class RealmPet: RealmObject(){
    @PrimaryKey
    var uid: String? = null
    var posts: RealmList<RealmPost>? = null
    var ownerId: String? = null
    var name: String? = null
    var imageUrl: String? = null
    var age: Int? = null
    var bio: String?= null
    var breed: String? = null
    var followers: RealmList<RealmUser>? = null
    var gender: String? = null
}


@RealmClass
open class RealmPost: RealmObject(){
    @PrimaryKey
    var uid: String? = null
    var title:String? = null
    var description:String? = null
    var filePath:String? = null
    var ownerId:String?= null
    var comments: RealmList<RealmComment>? = null
    var likes: RealmList<RealmLike>? = null
}

@RealmClass
open class RealmFollowingPost: RealmObject(){
    @PrimaryKey
    var listId: Int? = null
    var followingPost : RealmList<RealmPost>? = null
}

@RealmClass
open class RealmUserPost: RealmObject(){
    @PrimaryKey
    var listUserId: Int? = null
    var userAllPosts : RealmList<RealmPost>? = null
}

@RealmClass
open class RealmPetByOwner: RealmObject(){
    var petByOwner : RealmList<RealmPet>? = null
}

@RealmClass
open class RealmComment:RealmObject(){
    var comment:String? = null
    var postId: String? = null
    var userId:String? = null
}

@RealmClass
open class RealmLike:RealmObject(){
    var postId :String?=null
    var userId:String?= null

}