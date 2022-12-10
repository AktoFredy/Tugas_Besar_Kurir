package com.example.tubes1.Kiriman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tubes1.R
import com.example.tubes1.ResponseCreate
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityKirimanViewBinding
import com.example.tubes1.userSharedPreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewKirimanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKirimanViewBinding
    private val listKiriman = ArrayList<KirimanData>()
    private lateinit var prefManager: PrefManager
    private var idKirimanEdit: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kiriman_view)
        supportActionBar?.title = "KIRIMAN"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefManager = PrefManager(this)
        binding = ActivityKirimanViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setData()

        binding.delButon.setOnClickListener{
            deleteKiriman()
        }

        binding.editButon.setOnClickListener {
            val i = Intent(this@ViewKirimanActivity, EditKirimanActivity::class.java).apply {
                putExtra("idKirimanEdit", idKirimanEdit)
            }
            startActivity(i)
        }
    }

    private fun setData(){
        idKirimanEdit = intent.getIntExtra("idKiriman", -1)

        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.getDataKiriman(token_auth, idKirimanEdit.toString()).enqueue(object : Callback<ResponseDataKiriman>{
            override fun onResponse(
                call: Call<ResponseDataKiriman>,
                response: Response<ResponseDataKiriman>
            ) {
                if (response.isSuccessful){
                    listKiriman.clear()
                    response.body()?.let { listKiriman.addAll(it.data) }

                    if (listKiriman.size == 1){
                        with(binding){
                            idKirimanEdit = listKiriman[0].idK
                            OutDBNamaBarang.text = listKiriman[0].namaBar
                            OutDBBerat.text = listKiriman[0].berat.toString()
                            if (listKiriman[0].pchBlh == 1){
                                OutDBPecahBelah.text = "Iya"
                            }else{
                                OutDBPecahBelah.text = "Tidak"
                            }
                            OutDBCover.text = listKiriman[0].cover
                            if (listKiriman[0].asuransi == 1){
                                OutDBAsuransi.text = "Iya"
                            }else{
                                OutDBAsuransi.text = "Tidak"
                            }
                        }
                    }else{
                        for (i in listKiriman){
                            if (i.idK == idKirimanEdit){
                                with(binding){
                                    idKirimanEdit = i.idK
                                    OutDBNamaBarang.text = i.namaBar
                                    OutDBBerat.text = i.berat.toString()
                                    OutDBPecahBelah.text = i.pchBlh.toString()
                                    OutDBCover.text = i.cover
                                    OutDBAsuransi.text = i.asuransi.toString()

                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataKiriman>, t: Throwable) {

            }

        })
    }

    private fun deleteKiriman(){

        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.deleteDataKiriman(token_auth, idKirimanEdit).enqueue(object : Callback<ResponseCreate>{
            override fun onResponse(
                call: Call<ResponseCreate>,
                response: Response<ResponseCreate>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                    val i = Intent(this@ViewKirimanActivity, KirimanCRUDActivity::class.java)
                    startActivity(i)
                }else{
                    Toast.makeText(applicationContext, "${response.errorBody()}", Toast.LENGTH_LONG).show()
                    val i = Intent(this@ViewKirimanActivity, KirimanCRUDActivity::class.java)
                    startActivity(i)
                }
            }

            override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {

            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}