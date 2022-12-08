package com.example.tubes1.fragments.faq

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tubes1.R
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityAnswerFaqBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnswerFaqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnswerFaqBinding
    private val listFAQ = ArrayList<FaqData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "FAQ"

        binding = ActivityAnswerFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContent()
        binding.CsBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:" + "+6285669865451"))
            startActivity(intent)
        }
    }

    private fun setContent(){
        val intentID = intent.getIntExtra("intent_id", -1)
        server.instances.getDataFAQ(intentID.toString()).enqueue(object  : Callback<ResponseDataFaqs> {
            override fun onResponse(
                call: Call<ResponseDataFaqs>,
                response: Response<ResponseDataFaqs>
            ) {
                if (response.isSuccessful){
                    listFAQ.clear()
                    response.body()?.let { listFAQ.addAll(it.data) }

                    if (listFAQ.size == 1){
                        binding.fromPertanyaan.text = listFAQ[0].pertanyaan
                        binding.fromJawaban.text = listFAQ[0].jawaban
                    }else{
                        for (i in listFAQ){
                            if (i.idf == intentID){
                                binding.fromPertanyaan.text = i.pertanyaan
                                binding.fromJawaban.text = i.jawaban
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataFaqs>, t: Throwable) {

            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}