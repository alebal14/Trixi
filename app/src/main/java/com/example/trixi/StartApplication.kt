package com.example.trixi

import android.app.Application
import com.example.trixi.apiService.RetrofitClient

class StartApplication: Application() {


    override fun onCreate() {
        super.onCreate()

     RetrofitClient.context = this
    }

}