package com.example.tubes1.Penerima

import com.google.gson.annotations.SerializedName

data class PenerimaData(
    @SerializedName("id_penerima") val id_penerima: Int,
    @SerializedName("nama_penerima") val nama_penerima: String,
    @SerializedName("no_hp") val no_hp: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("kode_pos") val kode_pos: String
)
