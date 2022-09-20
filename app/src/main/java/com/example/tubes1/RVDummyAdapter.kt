package com.example.tubes1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.entity.dummy

class RVDummyAdapter(private val data: Array<dummy>) : RecyclerView.Adapter<RVDummyAdapter.viewHolder>(){
    //array untuk menyimpan image sesuai urutan dummy di entity (sudah di revisi dengan menggunakan images dari array yang ada di kelas dummy)
//    private val images = intArrayOf(R.drawable.bekasi, R.drawable.bogor, R.drawable.boyolali, R.drawable.gresik, R.drawable.kediri, R.drawable.kraton, R.drawable.kratonsolo, R.drawable.monas, R.drawable.surabaya, R.drawable.wonosobo)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_deliv, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int){
        val currentItem = data[position]
        holder.nama_kota.text = currentItem.namaKota
        holder.biaya.text = currentItem.biaya.toString()
        holder.waktu.text = currentItem.waktu
        holder.kota.setImageResource(currentItem.imgKota)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nama_kota : TextView = itemView.findViewById(R.id.nama_kota)
        val biaya : TextView = itemView.findViewById(R.id.biaya)
        val waktu : TextView = itemView.findViewById(R.id.waktu)
        val kota : ImageView = itemView.findViewById(R.id.iv_kota)
    }
}