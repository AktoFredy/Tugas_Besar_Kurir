package com.example.tubes1.dataCrud

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pengiriman(
    @PrimaryKey(autoGenerate = true)
    val idP: Int,
    val namaPengirim: String,
    val namaPenerima: String,
    val desBarang: String,
    val kotaAsal: String,
    val kotaTujuan: String,
    val alamatLengkap: String,
    val ongkos: Double
)
