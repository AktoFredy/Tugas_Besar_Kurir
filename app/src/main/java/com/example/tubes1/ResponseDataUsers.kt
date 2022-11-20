package com.example.tubes1

import com.google.gson.annotations.SerializedName

class ResponseDataUsers (
    @SerializedName("status") val stt:String,
    val totaldata: Int,
    val data:List<UsersData>
)