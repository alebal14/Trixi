package com.example.trixi.apiService

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit


class RetrofitClient {


    companion object{
       lateinit var context: Context
       var instance: Retrofit? = null

        //Anna
        val BASE_URL = "http://192.168.1.71:3000/"

        //Alexandra
        //val BASE_URL = "http://192.168.0.162:3000/"

        //Sofia
        //val BASE_URL = "http://192.168.0.2:3000/"

        //snehal
        //val BASE_URL = "http://192.168.0.166:3000/"

        //common ip address for all who are using Emulator
        //val BASE_URL = "http://10.0.2.2:3000/"

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

            //val client = okHttpClient().newBuilder().retryOnConnectionFailure(true).build()

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