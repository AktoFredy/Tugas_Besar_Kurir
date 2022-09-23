package com.example.tubes1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tubes1.dataCrud.CrudCons
import com.example.tubes1.databinding.ActivityEditBinding
import com.example.tubes1.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    lateinit var penampung_username: String
    lateinit var penampung_password: String
    lateinit var penampung_tgl: String
    var penampung_uID: Int = 0
    var data_kliriman: Int = 0

    //database
    val dbU by lazy { UserDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_edit_profile)
        supportActionBar?.setTitle("Editing Profile")

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        penampung_username = ""
        penampung_password=""
        penampung_tgl=""
        setupView()
        setupListenerEditUser()

    }

    fun setupListenerEditUser(){
        binding.btnSave.setOnClickListener{
            if(binding.inputPassword1.editText?.text.toString() == binding.inputConfirmPassword.editText?.text.toString()){
                CoroutineScope(Dispatchers.IO).launch {
                    dbU.userDao().updateUser(
                        User(penampung_uID, binding.inputEmail.editText?.text.toString(), binding.inputUsername.editText?.text.toString(),
                            binding.inputPassword1.editText?.text.toString(), penampung_tgl, binding.inputTlp.editText?.text.toString())
                    )
                    finish()
                }
            }else{
                binding.inputConfirmPassword.setError("Password Confirm Harus Sama")
            }
        }
    }

    fun setupView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            CrudCons.TYPE_UPDATE -> {
                getUser()
            }
        }
    }

    fun getUser(){
        data_kliriman = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbU.userDao().getUser2(data_kliriman)[0]
            binding.inputEmail.editText?.setText(user.email)
            binding.inputUsername.editText?.setText(user.username)
            binding.inputTlp.editText?.setText(user.noTelepon)
            penampung_uID = user.idU
            penampung_tgl = user.tanggalLahir
            penampung_password = user.password
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}