package com.rifqipadisiliwangi.sismartpju.data.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", Credentials.basic("kedaireka", "kedaireka212"))
            .build()
        return chain.proceed(request)
    }
}