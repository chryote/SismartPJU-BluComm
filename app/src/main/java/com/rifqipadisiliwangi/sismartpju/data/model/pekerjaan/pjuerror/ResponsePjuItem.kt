package com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuerror

data class ResponsePjuItem(
    val Respon_code: String,
    val Respon_desc: String,
    val tipe: List<Tipe>
)

data class Tipe(
    val alamat: String,
    val idkoordinator: String,
    val idne: String,
    val idpelanggan: String,
    val idperangkat: String,
    val idpju: String,
    val jenislampu: String,
    val jenislpju: String,
    val jumlahtiang: String,
    val kecamatan: String,
    val kelurahan: String,
    val klasifikasi: String,
    val kondisi: String,
    val latitude: String,
    val longitude: String,
    val nama: String,
    val tgl: String
)