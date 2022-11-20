package com.example.tubes1.client

import com.example.tubes1.api.apiPengiriman
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object server {
    public const val BASE_URL = "http://192.168.187.51/pi4-apipengiriman/public/"

    val instances:apiPengiriman by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(apiPengiriman::class.java)

    }
}