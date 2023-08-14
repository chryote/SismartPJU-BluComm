package com.rifqipadisiliwangi.sismartpju.data.network

import android.util.Base64
import androidx.datastore.preferences.protobuf.Api
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private val retrofit: Retrofit
    init {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                Interceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .addHeader("Authorization", AUTH)
                        .method(original.method, original.body)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
            ).build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val api: ApiService
        get() = retrofit.create(ApiService::class.java)

    companion object {
        private val AUTH =
            "Basic " + Base64.encodeToString("RGlzaHVidXNlcjIxMjp1c2VyRGlzaHViMjEy".toByteArray(), Base64.NO_WRAP)
        private const val BASE_URL = "https://sisemarpju.smartlinks.id/"
        private var mInstance: ApiClient? = null

        @get:Synchronized
        val instance: ApiClient?
            get() {
                if (mInstance == null) {
                    mInstance = ApiClient()
                }
                return mInstance
            }
    }
}