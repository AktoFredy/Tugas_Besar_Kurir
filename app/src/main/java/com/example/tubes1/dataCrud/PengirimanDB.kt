package com.example.tubes1.dataCrud

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tubes1.EditActivity
import com.example.tubes1.MainActivity
import com.example.tubes1.fragments.DeliveryFragment

@Database(
    entities = [Pengiriman::class],
    version = 1
)

abstract class PengirimanDB: RoomDatabase() {
    abstract fun PengirimanDao() : PengirimanDao

    companion object{
        @Volatile private var instance : PengirimanDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PengirimanDB::class.java,
            "pengiriman12345.db"
        ).build()
    }
}