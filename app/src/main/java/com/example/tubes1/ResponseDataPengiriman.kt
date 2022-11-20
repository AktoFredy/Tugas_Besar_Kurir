package com.example.tubes1

import com.google.gson.annotations.SerializedName

class ResponseDataPengiriman(
    @SerializedName("status") val stt:String,
    val totaldata: Int,
    val data:List<PengirimanData>
    )