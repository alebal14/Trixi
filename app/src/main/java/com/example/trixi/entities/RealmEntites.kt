package com.example.trixi.entities

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.io.File

@RealmClass
open  class RealmUserEntity: RealmObject(){
    @PrimaryKey
    //var id: Int? = null
    var uid: String? = null
    var userName: String? = null
    var email: String? = null
    var bio: String? = null
    var imageUrl: String? = null
    var role: String? = null
    //var pets: RealmList<Pet>? = RealmList()
  //  var posts: RealmList<Post>? = RealmList()

}


@RealmClass
open  class RealmPetEntity: RealmObject(){
    @PrimaryKey
    var uid: String? = null
   // var posts: RealmList<Post>? = null
    var ownerId: String? = null
    var name: String? = null
    var imageUrl: String? = null
    var age: Int? = null
    var bio: String?= null
    var breed: String? = null
   // var followers: RealmList<User>? = null
    var gender: String? = null
}


@RealmClass
open class RealmPostEntity: RealmObject(){
    @PrimaryKey
    var uid: String? = null
    var title:String? = null
    var description:String? = null
    var filePath:String? = null
    var ownerId:String?= null
  //  var comments: RealmList<Comment>? = null
  //  var likes: RealmList<Like>? = null
}