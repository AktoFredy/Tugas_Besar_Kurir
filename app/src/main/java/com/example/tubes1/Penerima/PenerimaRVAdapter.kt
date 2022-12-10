package com.example.tubes1.Penerima

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.databinding.RvItemPenerimaBinding

class PenerimaRVAdapter(private val penerimas: ArrayList<PenerimaData>, private val context: Context): RecyclerView.Adapter<PenerimaRVAdapter.PenerimaHolder>() {
    inner class PenerimaHolder(item: RvItemPenerimaBinding): RecyclerView.ViewHolder(item.root){
        private val binding = item
        fun bind(penerimaData: PenerimaData){
            with(binding){
                penerimaout.text = penerimaData.nama_penerima
                penerimaout.setOnClickListener {
                    context.startActivity(Intent(context, ViewPenerimaActivity::class.java).apply {
                        putExtra("idPenerima", penerimaData.id_penerima)
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenerimaHolder {
        return PenerimaHolder(RvItemPenerimaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PenerimaHolder, position: Int) {
        holder.bind(penerimas[position])
    }

    override fun getItemCount() = penerimas.size
}