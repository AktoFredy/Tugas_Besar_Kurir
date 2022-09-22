package com.example.tubes1.dataCrud

import androidx.room.*

@Dao
interface PengirimanDao {

    @Insert
    suspend fun addPengiriman(Pengiriman: Pengiriman)

    @Update
    suspend fun updatePengiriman(Pengiriman: Pengiriman)

    @Delete
    suspend fun deletePengiriman(Pengiriman: Pengiriman)

    @Query("SELECT * FROM pengiriman")
    suspend fun getPengiriman1() : List<Pengiriman>

    @Query("SELECT * FROM Pengiriman WHERE idP =:pengiriman_id")
    suspend fun getPengiriman2(pengiriman_id: Int) : List<Pengiriman>
}