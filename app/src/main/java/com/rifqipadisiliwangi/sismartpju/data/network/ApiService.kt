package com.rifqipadisiliwangi.sismartpju.data.network

import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginRequestIem
import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginResponseItem
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuerror.ResponsePjuItem
import com.rifqipadisiliwangi.sismartpju.data.utils.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Authorization: Basic RGlzaHVidXNlcjIxMjp1c2VyRGlzaHViMjEy")
    @POST("dd163577ea063b814f85b490a748d583?")
    suspend fun login(
//        @Field("username") username: String,
//        @Field("password") password: String
        @Body requestBody: LoginRequestIem
    ): Response<LoginResponseItem>

    @Headers("Authorization: Basic RGlzaHViZGF0YWxwanUyMTI6ZGF0YWxwanVEaXNodWIyMTI=")
    @GET(Constants.PJU_ERROR)
    fun getPju():Call<ResponsePjuItem>

}
