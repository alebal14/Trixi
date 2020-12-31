package com.example.trixi.apiService

import android.content.Context
import android.text.format.Formatter.formatIpAddress
import android.util.Log
import com.example.trixi.ui.fragments.PopUpCommentWindow.Companion.TAG
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.*
import java.util.*
import java.util.concurrent.TimeUnit


class RetrofitClient {


    companion object{
       lateinit var context: Context
       var instance: Retrofit? = null


       val BASE_URL = "http://192.168.0.162:3000/"


        fun okHttpClient() : OkHttpClient {
            var cookieManager: CookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            val cookieJar = MyPersistentCookieJar(context)
            val client = OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES) // write timeout
                .readTimeout(2, TimeUnit.MINUTES) // read timeout
            .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
            ).cookieJar(cookieJar)
                .build()
                return client
            }

        var gson = GsonBuilder()
            .setLenient()
            .create()




        fun getRetroInstance(): Retrofit? {
            if (instance == null){
                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient())
                    .build()

            }
            return instance
        }
    }




}