package com.example.tubes1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.tubes1.client.server
//import com.example.tubes1.dataCrud.CrudCons
//import com.example.tubes1.dataCrud.Pengiriman
//import com.example.tubes1.dataCrud.PengirimanDB
import com.example.tubes1.databinding.ActivityEditBinding
import com.example.tubes1.userSharedPreferences.PrefManager
import kotlinx.android.synthetic.main.activity_edit.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {
//    val db by lazy { PengirimanDB(this) }
    private var Biaya: Double = 0.00
    private var idPengiriman: Int = 0
    private lateinit var binding: ActivityEditBinding
    private val listPengiriman = ArrayList<PengirimanData>()
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.setTitle("Pengirimman")

        binding = ActivityEditBinding.inflate(layoutInflater)
        prefManager = PrefManager(this)

        setContentView(binding.root)
        setupView()
        setupListener()
    }

    override fun onResume() {
        super.onResume()
        val kotaPenerima = resources.getStringArray(R.array.kotaPenerima)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, kotaPenerima)
        binding.autoCompleteKotaPenerima.setAdapter(arrayAdapter)
        binding.autoCompleteKotaPengirim.setAdapter(arrayAdapter)
    }

    fun biayacost(){
        if(binding.inputKotaPenerima.editText?.text.toString() == "Jakarta"){
            Biaya = 50000.00
        }else if(binding.inputKotaPenerima.editText?.text.toString() == "Yogyakarta"){
            Biaya = 20000.00
        }else{
            Biaya = 25000.00
        }
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentID = intent.getIntExtra("intent_id", -1)
        idPengiriman = intentID
        if (intentID < 0){
            binding.btnUpdate.visibility = View.GONE
        }else{
            binding.btnSave.visibility = View.GONE
            getPengiriman()
        }
    }

    fun setupListener(){
        binding.btnSave.setOnClickListener{
            with(binding){
                val namaPengirim = inputPengirim.editText?.text.toString()
                val namaPenerima = inputPenerima.editText?.text.toString()
                val desBarang = inputIsiKiriman.editText?.text.toString()
                val kotaAsal = inputKotaPengirim.editText?.text.toString()
                val kotaTujuan = inputKotaPenerima.editText?.text.toString()
                val alamatLengkap = inputAlamatLengkap.editText?.text.toString()
                biayacost()

                val token_auth = "Bearer ${prefManager.getToken()}"

                server.instances.createData(token_auth,namaPengirim,namaPenerima,desBarang,kotaAsal,kotaTujuan,alamatLengkap,Biaya).enqueue(object :
                    Callback<ResponseCreate> {
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                    }

                })
            }


        }

        binding.btnUpdate.setOnClickListener{
            with(binding){
                val namaPengirim = inputPengirim.editText?.text.toString()
                val namaPenerima = inputPenerima.editText?.text.toString()
                val desBarang = inputIsiKiriman.editText?.text.toString()
                val kotaAsal = inputKotaPengirim.editText?.text.toString()
                val kotaTujuan = inputKotaPenerima.editText?.text.toString()
                val alamatLengkap = inputAlamatLengkap.editText?.text.toString()
                biayacost()

                val token_auth = "Bearer ${prefManager.getToken()}"

                server.instances.updateData(token_auth,idPengiriman,namaPengirim,namaPenerima,desBarang,kotaAsal,kotaTujuan,alamatLengkap,Biaya).enqueue(object :
                Callback<ResponseCreate>{
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if (response.isSuccessful){
                            Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {}
                })
            }

        }
    }

    fun getPengiriman(){
        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.getData(token_auth,idPengiriman.toString()).enqueue(object : Callback<ResponseDataPengiriman> {
            override fun onResponse(
                call: Call<ResponseDataPengiriman>,
                response: Response<ResponseDataPengiriman>
            ){
                if (response.isSuccessful){
                    listPengiriman.clear()
                    response.body()?.let { listPengiriman.addAll(it.data) }

                    if (listPengiriman.size == 1){
                        binding.inputPengirim.getEditText()?.setText(listPengiriman[0].namaPengirim)
                        binding.inputPenerima.getEditText()?.setText(listPengiriman[0].namaPenerima)
                        binding.inputIsiKiriman.getEditText()?.setText(listPengiriman[0].desBarang)
                        binding.inputKotaPengirim.autoCompleteKotaPengirim.setText(listPengiriman[0].kotaAsal, false)
                        binding.inputKotaPenerima.autoCompleteKotaPenerima.setText(listPengiriman[0].kotaTujuan, false)
                        binding.inputAlamatLengkap.getEditText()?.setText(listPengiriman[0].alamatLengkap)
                    }else{
                        for (i in listPengiriman){
                            if (i.idP == idPengiriman){
                                binding.inputPengirim.getEditText()?.setText(i.namaPengirim)
                                binding.inputPenerima.getEditText()?.setText(i.namaPenerima)
                                binding.inputIsiKiriman.getEditText()?.setText(i.desBarang)
                                binding.inputKotaPengirim.autoCompleteKotaPengirim.setText(i.kotaAsal, false)
                                binding.inputKotaPenerima.autoCompleteKotaPenerima.setText(i.kotaTujuan, false)
                                binding.inputAlamatLengkap.getEditText()?.setText(i.alamatLengkap)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataPengiriman>, t: Throwable) {}
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}