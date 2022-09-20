package com.example.tubes1.entity

import android.media.Image
import com.example.tubes1.R

class dummy (var namaKota: String, var biaya: Int, var waktu: String, val imgKota: Int){
    companion object{
        @JvmField
        var listOfDummy = arrayOf(
            dummy("Bekasi", 50000, "12 Jam", R.drawable.bekasi),
            dummy("Bogor", 40000, "10 Jam", R.drawable.bogor),
            dummy("Boyolali", 10000, "3 Jam", R.drawable.boyolali),
            dummy("Gresik", 40000, "9 Jam", R.drawable.gresik),
            dummy("Kediri", 50000, "11 Jam", R.drawable.kediri),
            dummy("Jogja", 10000, "1 Jam", R.drawable.kraton),
            dummy("Solo", 20000, "2 Jam", R.drawable.kratonsolo),
            dummy("Jakarta", 50000, "11 Jam", R.drawable.monas),
            dummy("Surabaya", 60000, "13 Jam", R.drawable.surabaya),
            dummy("Wonosobo", 30000, "5 Jam", R.drawable.wonosobo)
        )
    }
}