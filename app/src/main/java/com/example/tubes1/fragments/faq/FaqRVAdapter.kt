package com.example.tubes1.fragments.faq

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.databinding.RvItemFaqBinding

class FaqRVAdapter(private val faqs: ArrayList<FaqData>, private val context: Context) : RecyclerView.Adapter<FaqRVAdapter.FaqHolder>(){
    inner class FaqHolder(item:RvItemFaqBinding):RecyclerView.ViewHolder(item.root){
        private val binding = item
        fun bind(faqData: FaqData){
            with(binding){
                faqlist.text = faqData.pertanyaan
                faqlist.setOnClickListener{
                    var i = Intent(context, AnswerFaqActivity::class.java).apply {
                        putExtra("intent_id", faqData.idf)
                    }
                    context.startActivity(i)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqHolder {
        return FaqHolder(RvItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FaqHolder, position: Int) {
        holder.bind(faqs[position])
    }

    override fun getItemCount() = faqs.size
}