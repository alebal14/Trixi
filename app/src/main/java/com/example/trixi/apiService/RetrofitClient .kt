package com.example.trixi.apiService

import android.content.Context
import android.util.Base64
import okhttp3.JavaNetCookieJar

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy

class RetrofitClient {


    companion object{
       lateinit var context: Context
       var instance: Retrofit? = null
       val BASE_URL = "http://192.168.1.7:3000/rest/"

        fun okHttpClient() : OkHttpClient {
            var cookieManager: CookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            val cookieJar = MyPersistentCookieJar(context)
            val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            ).cookieJar(cookieJar).build()
                return client
            }


        fun getRetroInstance(): Retrofit? {
            if (instance == null){
                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient())
                    .build()
            }
            return instance
        }
    }




}