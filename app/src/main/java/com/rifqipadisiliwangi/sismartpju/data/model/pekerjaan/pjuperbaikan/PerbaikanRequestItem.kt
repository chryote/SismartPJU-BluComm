package com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuperbaikan

data class PerbaikanRequestItem(
    val idne: Int,
    val tgl: Int,
    val link: Int,
    val usernya: Int,
    val idpju: Int,
    val idpelanggan: Int,
    val tanggalperbaikan: Int,
    val foto1: Int,
    val foto2: Int,
    val hasilperbaikan: Int,
    val keteranganlainnya: Int,
    val jenisperbaikan: Int,
)