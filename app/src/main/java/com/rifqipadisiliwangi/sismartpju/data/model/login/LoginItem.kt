package com.rifqipadisiliwangi.sismartpju.data.model.login

data class LoginSuccessResponse(
    val Respon_code: String,
    val Respon_desc: String,
    val tipe: List<Tipe>
)

data class Tipe(
    val statusonline: String,
    val statususer: String
)