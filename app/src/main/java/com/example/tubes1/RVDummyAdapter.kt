package com.example.tubes1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.dataCrud.Pengiriman
import com.example.tubes1.databinding.RvItemDelivBinding
import com.example.tubes1.entity.dummy

class RVDummyAdapter(private val pengirimans: ArrayList<Pengiriman>, private val listener: OnAdapterListener) : RecyclerView.Adapter<RVDummyAdapter.PengirimanHolder>(){
    //array untuk menyimpan image sesuai urutan dummy di entity (sudah di revisi dengan menggunakan images dari array yang ada di kelas dummy)
//    private val images = intArrayOf(R.drawable.bekasi, R.drawable.bogor, R.drawable.boyolali, R.drawable.gresik, R.drawable.kediri, R.drawable.kraton, R.drawable.kratonsolo, R.drawable.monas, R.drawable.surabaya, R.drawable.wonosobo)
//    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val kotaAsal: TextView
//        val kotaTujuan: TextView
//        val namaPengirim: TextView
//        val namaPenerima: TextView
//        val biaya: TextView
//        val btnEdit: Button
//        val btnDelete: Button
//        val layClick: ConstraintLayout
//
//        init {
//            kotaAsal = itemView.findViewById(R.id.nama_kota_asal)
//            kotaTujuan = itemView.findViewById(R.id.nama_kota_tujuan)
//            namaPengirim = itemView.findViewById(R.id.nama_pengirim)
//            namaPenerima = itemView.findViewById(R.id.nama_penerima)
//            biaya = itemView.findViewById(R.id.biaya)
//            btnEdit = itemView.findViewById(R.id.edtBtn)
//            btnDelete = itemView.findViewById(R.id.delBtn)
//            layClick = itemView.findViewById(R.id.layout_de)
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PengirimanHolder {
        val itemView = RvItemDelivBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PengirimanHolder(itemView)
    }

    override fun onBindViewHolder(holder: PengirimanHolder, position: Int){
        val currentItem = pengirimans[position]
        holder.binding.namaKotaAsal.text = currentItem.kotaAsal
        holder.binding.namaKotaTujuan.text = currentItem.kotaTujuan
        holder.binding.namaPengirim.text = currentItem.namaPengirim
        holder.binding.namaPenerima.text = currentItem.namaPenerima
        holder.binding.biaya.text = currentItem.ongkos.toString()
        holder.binding.layoutDe.setOnClickListener{
            listener.onClick(currentItem)
        }
        holder.binding.edtBtn.setOnClickListener {
            listener.onUpdate(currentItem)
        }
        holder.binding.delBtn.setOnClickListener {
            listener.onDelete(currentItem)
        }
//        holder.kotaTujuan.text = currentItem.kotaTujuan
//        holder.namaPengirim.text = currentItem.namaPengirim
//        holder.namaPenerima.text = currentItem.namaPenerima
//        holder.biaya.text = currentItem.ongkos.toString()
//        holder.layClick.setOnClickListener{
//            listener.onClick(currentItem)
//        }
//        holder.btnEdit.setOnClickListener {
//            listener.onUpdate(currentItem)
//        }
//        holder.btnDelete.setOnClickListener {
//            listener.onDelete(currentItem)
//        }
//        holder.nama_kota.text = currentItem.namaKota
//        holder.biaya.text = currentItem.biaya.toString()
//        holder.waktu.text = currentItem.waktu
//        holder.kota.setImageResource(currentItem.imgKota)
    }

    override fun getItemCount() = pengirimans.size
    inner class PengirimanHolder(var binding : RvItemDelivBinding) : RecyclerView.ViewHolder(binding.root)

//    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
////        val nama_kota : TextView = itemView.findViewById(R.id.nama_kota)
////        val biaya : TextView = itemView.findViewById(R.id.biaya)
////        val waktu : TextView = itemView.findViewById(R.id.waktu)
////        val kota : ImageView = itemView.findViewById(R.id.iv_kota)
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Pengiriman>){
        pengirimans.clear()
        pengirimans.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(pengiriman: Pengiriman)
        fun onUpdate(pengiriman: Pengiriman)
        fun onDelete(pengiriman: Pengiriman)
    }
}