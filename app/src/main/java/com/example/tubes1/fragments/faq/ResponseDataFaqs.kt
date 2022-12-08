package com.example.tubes1.fragments.faq

import com.google.gson.annotations.SerializedName

class ResponseDataFaqs(
    @SerializedName("status") val stt:String,
    val totalData: Int,
    val data: List<FaqData>
)