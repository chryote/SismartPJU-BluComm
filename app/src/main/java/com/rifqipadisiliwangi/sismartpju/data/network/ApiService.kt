package com.rifqipadisiliwangi.sismartpju.data.network

import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuerror.ResponsePjuItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers("Authorization: Basic RGlzaHViZGF0YWxwanUyMTI6ZGF0YWxwanVEaXNodWIyMTI=")
    @GET("462bac86fe8f8d315f73006bda73a088?") // Ganti dengan endpoint API yang sesuai
    fun getPju():Call<ResponsePjuItem>

}
