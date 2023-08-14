package com.rifqipadisiliwangi.sismartpju.data.network


import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {
    @FormUrlEncoded
    @POST("dd163577ea063b814f85b490a748d583?")
    fun userLogin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>
}
