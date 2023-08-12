package com.rifqipadisiliwangi.sismartpju.data.network

import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginItem
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("https://sisemarpju.smartlinks.id/dd163577ea063b814f85b490a748d583?") // Replace "login" with your actual endpoint
    fun login(
        @Field("username") phone: String,
        @Field("password") password: String
    ): Call<LoginItem>
}
