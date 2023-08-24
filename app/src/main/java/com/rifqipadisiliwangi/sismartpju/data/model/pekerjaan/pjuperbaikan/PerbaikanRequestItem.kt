package com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuperbaikan

data class PerbaikanRequestItem(
    val idne: String,
    val tgl: String,
    val link: String,
    val usernya: String,
    val idpju: String,
    val idpelanggan: String,
    val tanggalperbaikan: String,
    val foto1: String,
    val foto2: String,
    val hasilperbaikan: String,
    val keteranganlainnya: String,
    val jenisperbaikan: String,
)