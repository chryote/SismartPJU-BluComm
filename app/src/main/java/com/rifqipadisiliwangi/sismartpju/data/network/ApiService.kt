package com.rifqipadisiliwangi.sismartpju.data.network

import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginResponse
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuerror.ResponsePjuItem
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuerror.TipePju
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @Headers("Authorization: Basic RGlzaHViZGF0YWxwanUyMTI6ZGF0YWxwanVEaXNodWIyMTI=")
    @GET("462bac86fe8f8d315f73006bda73a088?") // Ganti dengan endpoint API yang sesuai
    fun getPju():Call<ResponsePjuItem>

}
