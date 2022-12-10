package com.example.tubes1.Kiriman

import com.google.gson.annotations.SerializedName

data class KirimanData(
    @SerializedName("id_kiriman") val idK: Int,
    @SerializedName("nama_barang") val namaBar: String,
    @SerializedName("berat") val berat: Int,
    @SerializedName("pecah_belah") val pchBlh: Int,
    @SerializedName("cover") val cover: String,
    @SerializedName("asuransi") val asuransi: Int,
)
