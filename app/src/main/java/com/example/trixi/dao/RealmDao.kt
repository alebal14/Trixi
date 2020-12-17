package com.example.trixi.dao

import io.realm.RealmModel
import io.realm.RealmResults

class RealmDao
fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLivedata<T>(this)