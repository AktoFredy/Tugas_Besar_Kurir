package com.example.tubes1.api

import com.example.tubes1.ResponseCreate
import com.example.tubes1.ResponseDataPengiriman
import com.example.tubes1.ResponseDataUsers
import com.example.tubes1.ResponseLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface apiPengiriman {
    @GET("pengiriman/{cari}")
    fun getData(
        @Header("Authorization") token_auth: String?,
        @Path("cari") cari:String? = null
    ): Call<ResponseDataPengiriman>

    @FormUrlEncoded
    @POST("pengiriman")
    fun createData(
        @Header("Authorization") token_auth: String?,
        @Field("namaPengirim") namaPengirim:String?,
        @Field("namaPenerima") namaPenerima:String?,
        @Field("desBarang") desBarang:String?,
        @Field("kotaAsal") kotaAsal:String?,
        @Field("kotaTujuan") kotaTujuan:String?,
        @Field("alamatLengkap") alamatLengkap:String?,
        @Field("ongkos") ongkos:Double?,
    ): Call<ResponseCreate>

    @DELETE("pengiriman/{idP}")
    fun deleteData(
        @Header("Authorization") token_auth: String?,
        @Path("idP")id: Int?
    ): Call<ResponseCreate>

    @FormUrlEncoded
    @PUT("pengiriman/{idP}")
    fun updateData(
        @Header("Authorization") token_auth: String?,
        @Path("idP") idP:Int?,
        @Field("namaPengirim") namaPengirim:String?,
        @Field("namaPenerima") namaPenerima:String?,
        @Field("desBarang") desBarang:String?,
        @Field("kotaAsal") kotaAsal:String?,
        @Field("kotaTujuan") kotaTujuan:String?,
        @Field("alamatLengkap") alamatLengkap:String?,
        @Field("ongkos") ongkos:Double?,
    ): Call<ResponseCreate>

    @GET("user/{cari}")
    fun getDataUser(
        @Header("Authorization") token_auth: String?,
        @Path("cari") cari:String? = null
    ): Call<ResponseDataUsers>

    @FormUrlEncoded
    @POST("user")
    fun createDataUser(
//        @Header("Authorization") token_auth: String?,
        @Field("useremail") email:String?,
        @Field("username") username:String?,
        @Field("userpassword") password:String?,
        @Field("confirmpassword") confirm:String?,
        @Field("tanggalLahir") tanggalLahir:String?,
        @Field("noTelepon") noTelepon:String?,
    ): Call<ResponseCreate>

    @DELETE("user/{idU}")
    fun deleteDataUser(
        @Header("Authorization") token_auth: String?,
        @Path("idU")id: Int?
    ): Call<ResponseCreate>

    @FormUrlEncoded
    @PUT("user/{idU}")
    fun updateDataUser(
        @Header("Authorization") token_auth: String?,
        @Path("idU") idU:Int?,
        @Field("useremail") email:String?,
        @Field("username") username:String?,
        @Field("userpassword") password:String?,
        @Field("noTelepon") noTelepon:String?,
    ): Call<ResponseCreate>

    @GET("login")
    fun cekLoginUser(
        @Query("username") username: String,
        @Query("userpassword") userpassword: String,
    ):Call<ResponseLogin>

    @Multipart
    @POST("upload/{idU}")
    fun uploadUserImage(
        @Path("idU") idU: Int?,
        @Part userfoto: MultipartBody.Part,
        @Part("_method") _method:RequestBody
    ):Call<ResponseCreate>
}