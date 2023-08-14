package com.rifqipadisiliwangi.sismartpju.data.model.login

data class LoginResponse(
    val tipe: List<TipeList>,
    val Respon_code: String,
    val Respon_desc: String
)

data class TipeList(
    val statusonline: String,
    val statususer: String
)