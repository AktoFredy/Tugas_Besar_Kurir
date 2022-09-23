package com.example.tubes1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val idU: Int,
    val email: String,
    val username: String,
    val password: String,
    val tanggalLahir: String,
    val noTelepon: String

)