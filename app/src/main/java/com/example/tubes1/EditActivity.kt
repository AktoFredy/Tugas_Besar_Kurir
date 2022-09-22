package com.example.tubes1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tubes1.dataCrud.CrudCons
import com.example.tubes1.dataCrud.Pengiriman
import com.example.tubes1.dataCrud.PengirimanDB
import com.example.tubes1.databinding.ActivityEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    val db by lazy { PengirimanDB(this) }
    private var Biaya: Double = 0.00
    private var idPengiriman: Int = 0
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //biayacost()
        setupView()
        setupListener()
    }

    fun biayacost(){
        if(binding.inputKotaPenerima.toString() == "Jakarta"){
            Biaya = 50000.00
        }else if(binding.inputKotaPenerima.toString() == "Jogja"){
            Biaya = 20000.00
        }else{
            Biaya = 25000.00
        }
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when(intentType){
            CrudCons.TYPE_CREATE -> {
                binding.btnUpdate.visibility = View.GONE
            }
            CrudCons.TYPE_READ -> {
                binding.btnUpdate.visibility = View.GONE
                binding.btnSave.visibility = View.GONE
                getPengiriman()
            }
            CrudCons.TYPE_UPDATE -> {
                binding.btnSave.visibility = View.GONE
                getPengiriman()
            }
        }
    }

    fun setupListener(){
        binding.btnSave.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                db.PengirimanDao().addPengiriman(
                    Pengiriman(0, binding.inputPengirim.editText?.text.toString(), binding.inputPenerima.editText?.text.toString(),
                        binding.inputIsiKiriman.editText?.text.toString(), binding.inputKotaPengirim.editText?.text.toString(), binding.inputKotaPenerima.editText?.text.toString(),
                        binding.inputAlamatLengkap.editText?.text.toString(), Biaya)
                )
                finish()
            }
        }

        binding.btnUpdate.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                db.PengirimanDao().updatePengiriman(
                    Pengiriman(idPengiriman, binding.inputPengirim.editText?.text.toString(), binding.inputPenerima.editText?.text.toString(),
                        binding.inputIsiKiriman.editText?.text.toString(), binding.inputKotaPengirim.editText?.text.toString(), binding.inputKotaPenerima.editText?.text.toString(),
                        binding.inputAlamatLengkap.editText?.text.toString(), Biaya)
                )
                finish()
            }
        }
    }

    fun getPengiriman(){
        idPengiriman = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.Main).launch {
            val pengirimans = db.PengirimanDao().getPengiriman2(idPengiriman)[0]
            binding.inputPengirim.getEditText()?.setText(pengirimans.namaPengirim)
            binding.inputPenerima.getEditText()?.setText(pengirimans.namaPenerima)
            binding.inputIsiKiriman.getEditText()?.setText(pengirimans.desBarang)
            binding.inputKotaPengirim.getEditText()?.setText(pengirimans.kotaAsal)
            binding.inputKotaPenerima.getEditText()?.setText(pengirimans.kotaTujuan)
            binding.inputAlamatLengkap.getEditText()?.setText(pengirimans.alamatLengkap)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}