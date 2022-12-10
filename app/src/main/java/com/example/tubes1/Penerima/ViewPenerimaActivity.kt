package com.example.tubes1.Penerima

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tubes1.ResponseCreate
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityViewPenerimaBinding
import com.example.tubes1.userSharedPreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewPenerimaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPenerimaBinding
    private val listPenerima = ArrayList<PenerimaData>()
    private lateinit var prefManager: PrefManager
    private var idPenerimaEdit: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "DATA PENERIMA"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefManager = PrefManager(this)
        binding = ActivityViewPenerimaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataPenerima()

        binding.delButonPenerima.setOnClickListener {
            deleteDataPenerima()
        }

        binding.editButon.setOnClickListener {
            startActivity(Intent(this@ViewPenerimaActivity, EditPenerimaActivity::class.java).apply {
                putExtra("idPenerimaEdit", idPenerimaEdit)
            })
        }
    }

    private fun setDataPenerima(){
        idPenerimaEdit = intent.getIntExtra("idPenerima", -1)

        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.getDataPenerima(token_auth, idPenerimaEdit.toString()).enqueue(object : Callback<ResponseDataPenerima>{
            override fun onResponse(
                call: Call<ResponseDataPenerima>,
                response: Response<ResponseDataPenerima>
            ) {
                if (response.isSuccessful){
                    listPenerima.clear()
                    response.body()?.let { listPenerima.addAll(it.data) }

                    if (listPenerima.size == 1){
                        with(binding){
                            idPenerimaEdit = listPenerima[0].id_penerima
                            OutDBNamaPenerima.text = listPenerima[0].nama_penerima
                            OutDBnoHp.text = listPenerima[0].no_hp
                            if (listPenerima[0].gender == 1){
                                OutDBGender.text = "Pria"
                            }else{
                                OutDBGender.text = "Wanita"
                            }
                            OutDBKodePos.text = listPenerima[0].kode_pos
                        }
                    }else{
                        for (i in listPenerima){
                            if (i.id_penerima == idPenerimaEdit){
                                with(binding){
                                    idPenerimaEdit = i.id_penerima
                                    OutDBNamaPenerima.text = i.nama_penerima
                                    OutDBnoHp.text = i.no_hp
                                    if (i.gender == 1){
                                        OutDBGender.text = "Pria"
                                    }else{
                                        OutDBGender.text = "Wanita"
                                    }
                                    OutDBKodePos.text = i.kode_pos
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataPenerima>, t: Throwable) {
            }

        })
    }

    private fun deleteDataPenerima(){
        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.deleteDataPenerima(token_auth, idPenerimaEdit).enqueue(object : Callback<ResponseCreate>{
            override fun onResponse(
                call: Call<ResponseCreate>,
                response: Response<ResponseCreate>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                    val i = Intent(this@ViewPenerimaActivity, PenerimaCRUDActivity::class.java)
                    startActivity(i)
                }else{
                    Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                    val i = Intent(this@ViewPenerimaActivity, PenerimaCRUDActivity::class.java)
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