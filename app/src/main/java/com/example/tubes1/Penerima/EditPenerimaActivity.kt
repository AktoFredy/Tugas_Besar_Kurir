package com.example.tubes1.Penerima

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.tubes1.R
import com.example.tubes1.ResponseCreate
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityEditPenerimaBinding
import com.example.tubes1.userSharedPreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPenerimaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPenerimaBinding
    private val listPenerima = ArrayList<PenerimaData>()
    private lateinit var prefManager: PrefManager
    private var idPenerima: Int = -1
    private var intentCreate: Int = 0
    private var statGen = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefManager = PrefManager(this)
        binding = ActivityEditPenerimaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idPenerima = intent.getIntExtra("idPenerimaEdit", -1)
        intentCreate = intent.getIntExtra("intentCreatePenerima", 0)

        if (intentCreate == 0){
            supportActionBar?.title = "EDIT DATA PENERIMA"
            setDataPenerima()

            binding.SaveButonPenerima.setOnClickListener {
                saveUpdatePenerima()
            }

            binding.CancelButonPenerima.setOnClickListener {
                finish()
            }
        }else{
            supportActionBar?.title = "NEW DATA PENERIMA"
            binding.titleEditViewerPenerima.text = "New Data Penerima"

            binding.SaveButonPenerima.setOnClickListener{
                createNewDataPenerima()
            }

            binding.CancelButonPenerima.setOnClickListener {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val dropdownGender = resources.getStringArray(R.array.Gender)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, dropdownGender)
        binding.OutDBGenderEdit.setAdapter(adapter)
    }

    private fun createNewDataPenerima(){
        val token = "Bearer ${prefManager.getToken()}"

        with(binding){
            val namaPenerima = OutDBNamaPenerimaEdit.text.toString()
            val nohp = OutDBNoHpEdit.text.toString()
            val gender = OutDBGenderEdit.text.toString()
            if (gender == "Pria" || gender == "Laki-Laki"){
                statGen = 1
            }
            val kode_pos = OutDBKodePosEdit.text.toString()

            server.instances.createDataPenerima(token, namaPenerima, nohp, statGen.toString(), kode_pos).enqueue(object : Callback<ResponseCreate>{
                override fun onResponse(
                    call: Call<ResponseCreate>,
                    response: Response<ResponseCreate>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@EditPenerimaActivity, PenerimaCRUDActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                }

            })
        }
    }

    private fun setDataPenerima(){
        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.getDataPenerima(token_auth, idPenerima.toString()).enqueue(object : Callback<ResponseDataPenerima>{
            override fun onResponse(
                call: Call<ResponseDataPenerima>,
                response: Response<ResponseDataPenerima>
            ) {
                if (response.isSuccessful){
                    listPenerima.clear()
                    response.body()?.let { listPenerima.addAll(it.data) }

                    if (listPenerima.size == 1){
                        with(binding){
                            OutDBNamaPenerimaEdit.setText(listPenerima[0].nama_penerima)
                            OutDBNoHpEdit.setText(listPenerima[0].no_hp)
                            if (listPenerima[0].gender == 1){
                                OutDBGenderEdit.setText("Pria")
                            }else{
                                OutDBGenderEdit.setText("Wanita")
                            }
                            OutDBKodePosEdit.setText(listPenerima[0].kode_pos)
                        }
                    }else{
                        for (i in listPenerima){
                            if (i.id_penerima == idPenerima){
                                with(binding){
                                    OutDBNamaPenerimaEdit.setText(i.nama_penerima)
                                    OutDBNoHpEdit.setText(i.no_hp)
                                    if (i.gender == 1){
                                        OutDBGenderEdit.setText("Pria")
                                    }else{
                                        OutDBGenderEdit.setText("Wanita")
                                    }
                                    OutDBKodePosEdit.setText(i.kode_pos)
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

    private fun saveUpdatePenerima(){
        val token_auth = "Bearer ${prefManager.getToken()}"

        with(binding){
            val namaPenerima = OutDBNamaPenerimaEdit.text.toString()
            val nohp = OutDBNoHpEdit.text.toString()
            val gender = OutDBGenderEdit.text.toString()
            if (gender == "Pria" || gender == "Laki-Laki"){
                statGen = 1
            }
            val kode_pos = OutDBKodePosEdit.text.toString()

            server.instances.updateDataPenerima(token_auth, idPenerima, namaPenerima, nohp, statGen.toString(), kode_pos).enqueue(object : Callback<ResponseCreate>{
                override fun onResponse(
                    call: Call<ResponseCreate>,
                    response: Response<ResponseCreate>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                        val i = Intent(this@EditPenerimaActivity, PenerimaCRUDActivity::class.java)
                        startActivity(i)
                    }else{
                        Toast.makeText(applicationContext, "${response.errorBody()}", Toast.LENGTH_LONG).show()
                        val i = Intent(this@EditPenerimaActivity, PenerimaCRUDActivity::class.java)
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