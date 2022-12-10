package com.example.tubes1.Penerima

import com.google.gson.annotations.SerializedName

class ResponseDataPenerima(
    @SerializedName("status") val stt:String,
    val totalData: Int,
    val data: List<PenerimaData>
)