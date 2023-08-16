package com.rifqipadisiliwangi.sismartpju.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
//    private const val BASE_URL = "https://sisemarpju.smartlinks.id/" // Ganti dengan URL API yang sesuai
//
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val apiService: ApiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }



    const val BASE_URL = "https://sisemarpju.smartlinks.id/"

    val instance : ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}