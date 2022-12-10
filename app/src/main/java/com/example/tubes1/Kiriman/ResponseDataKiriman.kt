package com.example.tubes1.Kiriman

import com.google.gson.annotations.SerializedName

class ResponseDataKiriman(
    @SerializedName("status") val stt:String,
    val totalData: Int,
    val data: List<KirimanData>
)