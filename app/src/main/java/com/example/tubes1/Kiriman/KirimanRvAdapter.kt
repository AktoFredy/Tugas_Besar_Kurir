package com.example.tubes1.Kiriman

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.databinding.RvItemKirimanBinding

class KirimanRvAdapter(private val kirimans: ArrayList<KirimanData>, private val context: Context): RecyclerView.Adapter<KirimanRvAdapter.KirimanHolder>() {
    inner class KirimanHolder(item: RvItemKirimanBinding): RecyclerView.ViewHolder(item.root){
        private val binding = item
        fun bind(kirimanData: KirimanData){
            with(binding){
                kirimanout.text = kirimanData.namaBar
                kirimanout.setOnClickListener{
                    val i = Intent(context, ViewKirimanActivity::class.java).apply {
                        putExtra("idKiriman", kirimanData.idK)
                    }
                    context.startActivity(i)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KirimanHolder {
        return KirimanHolder(RvItemKirimanBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: KirimanHolder, position: Int) {
        holder.bind(kirimans[position])
    }

    override fun getItemCount() = kirimans.size
}