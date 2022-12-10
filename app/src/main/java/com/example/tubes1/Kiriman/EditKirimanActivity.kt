package com.example.tubes1.Kiriman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.tubes1.R
import com.example.tubes1.ResponseCreate
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityEditKirimanBinding
import com.example.tubes1.userSharedPreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditKirimanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditKirimanBinding
    private val listKiriman = ArrayList<KirimanData>()
    private lateinit var prefManager: PrefManager
    private var idKiriman: Int = -1
    private var intentcreate: Int = 0
    private var statPch = 0
    private var statAs = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefManager = PrefManager(this)
        binding = ActivityEditKirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idKiriman = intent.getIntExtra("idKirimanEdit", -1)
        intentcreate = intent.getIntExtra("intentCreate", 0)

        if (intentcreate == 0){
            supportActionBar?.title = "EDIT KIRIMAN"
            setData()

            binding.SaveButon.setOnClickListener {
                saveUpdate()
            }

            binding.CancelButon.setOnClickListener {
                finish()
            }
        }else{
            supportActionBar?.title = "NEW KIRIMAN"
            binding.titleViewerKiriman.text = "New Data Kiriman"
            binding.SaveButon.setOnClickListener {
                createKiriman()
            }

            binding.CancelButon.setOnClickListener {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val dropdown = resources.getStringArray(R.array.falsetrue)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, dropdown)
        binding.OutDBAsuransiEdit.setAdapter(adapter)
        binding.OutDBPecahBelahEdit.setAdapter(adapter)
    }

    private fun createKiriman(){
        val token = "Bearer ${prefManager.getToken()}"

        with(binding){
            val namabarang = OutDBNamaBarangEdit.text.toString()
            val berat = OutDBBeratEdit.text.toString()
            val pechb = OutDBPecahBelahEdit.text.toString()
            if (pechb == "Iya"){
                statPch = 1
            }
            val cover = OutDBCoverEdit.text.toString()
            val asuransi = OutDBAsuransiEdit.text.toString()
            if (asuransi == "Iya"){
                statAs = 1
            }

            server.instances.createDataKiriman(token, namabarang, berat, statPch.toString(), cover, statAs.toString()).enqueue(object : Callback<ResponseCreate>{
                override fun onResponse(
                    call: Call<ResponseCreate>,
                    response: Response<ResponseCreate>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@EditKirimanActivity, KirimanCRUDActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                }

            })
        }
    }

    private fun setData(){

        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.getDataKiriman(token_auth, idKiriman.toString()).enqueue(object :
            Callback<ResponseDataKiriman> {
            override fun onResponse(
                call: Call<ResponseDataKiriman>,
                response: Response<ResponseDataKiriman>
            ) {
                if (response.isSuccessful){
                    listKiriman.clear()
                    response.body()?.let { listKiriman.addAll(it.data) }

                    if (listKiriman.size == 1){
                        with(binding){
                            OutDBNamaBarangEdit.setText(listKiriman[0].namaBar)
                            OutDBBeratEdit.setText(listKiriman[0].berat.toString())
                            if (listKiriman[0].pchBlh == 1){
                                OutDBPecahBelahEdit.setText("Iya")
                            }else{
                                OutDBPecahBelahEdit.setText("Tidak")
                            }
                            OutDBCoverEdit.setText(listKiriman[0].cover)
                            if (listKiriman[0].asuransi == 1){
                                OutDBAsuransiEdit.setText("Iya")
                            }else{
                                OutDBAsuransiEdit.setText("Tidak")
                            }
                        }
                    }else{
                        for (i in listKiriman){
                            if (i.idK == idKiriman){
                                with(binding){
                                    OutDBNamaBarangEdit.setText(i.namaBar)
                                    OutDBBeratEdit.setText(i.berat.toString())
                                    if (i.pchBlh == 1){
                                        OutDBAsuransiEdit.setText("Iya")
                                    }else{
                                        OutDBAsuransiEdit.setText("Tidak")
                                    }
                                    OutDBCoverEdit.setText(i.cover)
                                    if (i.asuransi == 1){
                                        OutDBAsuransiEdit.setText("Iya")
                                    }else{
                                        OutDBAsuransiEdit.setText("Tidak")
                                    }

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

    private fun saveUpdate(){
        val token_auth = "Bearer ${prefManager.getToken()}"


        with(binding){
            val namabar = OutDBNamaBarangEdit.text.toString()
            val berat = OutDBBeratEdit.text.toString()
            val pchblh = OutDBPecahBelahEdit.text.toString()
            if (pchblh == "Iya"){
                statPch = 1
            }
            val cover = OutDBCoverEdit.text.toString()
            val asuransi = OutDBAsuransiEdit.text.toString()
            if (asuransi == "Iya"){
                statAs = 1
            }

            server.instances.updateDataKiriman(token_auth, idKiriman, namabar, berat, statPch.toString(), cover, statAs.toString())
                .enqueue(object : Callback<ResponseCreate>{
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if (response.isSuccessful){
                            Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            val i = Intent(this@EditKirimanActivity, KirimanCRUDActivity::class.java)
                            startActivity(i)
                        }else{
                            Toast.makeText(applicationContext, "${response.errorBody()}", Toast.LENGTH_LONG).show()
                            val i = Intent(this@EditKirimanActivity, KirimanCRUDActivity::class.java)
                            startActivity(i)
                        }
                    }

                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {

                    }

                })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}