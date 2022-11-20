package com.example.tubes1

import com.google.gson.annotations.SerializedName

class UsersData (
    @SerializedName("idU") val idU: Int,
    @SerializedName("useremail") val email:String,
    @SerializedName("username") val username:String,
    @SerializedName("userpassword") val password:String,
    @SerializedName("tanggalLahir") val tanggalLahir:String,
    @SerializedName("noTelepon") val noTelepon:String,
)