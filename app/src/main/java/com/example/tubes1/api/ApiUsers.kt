package com.example.tubes1.api

import com.example.tubes1.ResponseCreate
import com.example.tubes1.ResponseDataUsers
import retrofit2.Call
import retrofit2.http.*

interface apiUsers {
    @GET("user/{cari}")
    fun getDataUser(@Path("cari") cari:String? = null): Call<ResponseDataUsers>

    @FormUrlEncoded
    @POST("user")
    fun createDataUser(
        @Field("email") email:String?,
        @Field("username") username:String?,
        @Field("password") password:String?,
        @Field("tanggalLahir") tanggalLahir:String?,
        @Field("noTelepon") noTelepon:String?,
    ): Call<ResponseCreate>

    @DELETE("user/{idU}")
    fun deleteDataUser(@Path("idU")id: Int?): Call<ResponseCreate>

    @FormUrlEncoded
    @PUT("user/{idU}")
    fun updateDataUser(
        @Path("idU") idU:Int?,
        @Field("email") email:String?,
        @Field("username") username:String?,
        @Field("password") password:String?,
        @Field("tanggalLahir") tanggalLahir:String?,
        @Field("noTelepon") noTelepon:String?,
    ): Call<ResponseCreate>
}