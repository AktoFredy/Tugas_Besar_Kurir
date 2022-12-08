package com.example.tubes1.fragments.faq

import com.google.gson.annotations.SerializedName

data class FaqData(
    @SerializedName("id") val idf: Int,
    @SerializedName("question") val pertanyaan: String,
    @SerializedName("answer") val jawaban: String,
)
