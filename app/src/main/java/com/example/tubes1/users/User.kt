package com.example.tubes1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val email: String,
    val username: String,
    val password: String,
    val tanggalLahir: Date,
    val noTelepon: String

)