package com.example.tubes1

import com.google.gson.annotations.SerializedName

data class PengirimanData (
    @SerializedName("idP") val idP: Int,
    @SerializedName("namaPengirim") val namaPengirim:String,
    @SerializedName("namaPenerima") val namaPenerima:String,
    @SerializedName("desBarang") val desBarang:String,
    @SerializedName("kotaAsal") val kotaAsal:String,
    @SerializedName("kotaTujuan") val kotaTujuan:String,
    @SerializedName("alamatLengkap") val alamatLengkap:String,
    @SerializedName("ongkos") val ongkos:Double,
)