package com.example.trixi

import android.app.Application
import com.example.trixi.apiService.RetrofitClient
import io.realm.Realm
import io.realm.RealmConfiguration

class StartApplication: Application() {


    override fun onCreate() {
        super.onCreate()
     RetrofitClient.context = this

        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
                .name("trixiDB")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(configuration)


    }

}