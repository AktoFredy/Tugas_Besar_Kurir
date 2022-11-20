package com.example.tubes1

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tubes1.client.server
//import com.example.tubes1.dataCrud.CrudCons
import com.example.tubes1.databinding.ActivityEditBinding
import com.example.tubes1.databinding.ActivityEditProfileBinding
import com.example.tubes1.fragments.ProfileFragment
import com.example.tubes1.userSharedPreferences.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private val listUsers = ArrayList<UsersData>()
    private lateinit var prefManager: PrefManager
    private lateinit var binding: ActivityEditProfileBinding

    lateinit var penampung_username: String
    lateinit var penampung_password: String
    lateinit var penampung_tgl: String
    var penampung_uID: Int = 0
    var data_kliriman: Int = 0

    //Shared Preference for login and register
    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setTitle("Editing Profile")

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)
        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)

        penampung_username = ""
        penampung_password=""
        penampung_tgl=""
        getUser()
        setupListenerEditUser()

    }

    fun setupListenerEditUser(){
        binding.btnSave.setOnClickListener{
            with(binding){
                val temp_email = editEmail.text.toString()
                val temp_username = editUsername.text.toString()
                val temp_password = editPassword1.text.toString()
                val temp_confirm = editConfirm.text.toString()
                val temp_tlp = editTlp.text.toString()

                if (temp_email.isEmpty() || temp_email == ""){
                    binding.inputEmail.setError("Email tidak boleh kosong!")
                }
                if (temp_username.isEmpty() || temp_username == ""){
                    binding.inputUsername.setError("Username tidak boleh kosong!")
                }
                if (temp_password.isEmpty() || temp_password == ""){
                    binding.inputPassword1.setError("Password tidak boleh kosong!")
                }
                if (temp_confirm.isEmpty() || temp_confirm == ""){
                    binding.inputConfirmPassword.setError("Confirm Password tidak boleh kosong!")
                }else if(temp_confirm != temp_password) {
                    binding.inputConfirmPassword.setError("Password dan Confrim Password harus sama!!")
                }
                if (temp_tlp.isEmpty() || temp_tlp == ""){
                    binding.inputTlp.setError("No Tlp Tidak boleh kosong")
                }else if (temp_tlp.length < 12){
                    binding.inputTlp.setError("No Tlp harus >= 12 Digit")
                }

                if (!temp_username.isEmpty() && temp_username != "" && !temp_email.isEmpty() && temp_email != "" && !temp_password.isEmpty() && temp_password != "" && !temp_confirm.isEmpty() && temp_confirm != "" && temp_confirm == temp_password && !temp_tlp.isEmpty() && temp_tlp != "" && temp_tlp.length >= 12){

                    val token_auth = "Bearer ${prefManager.getToken()}"

                    server.instances.updateDataUser(token_auth, penampung_uID, temp_email, temp_username, temp_password, temp_tlp).enqueue(object : Callback<ResponseCreate>{
                        override fun onResponse(
                            call: Call<ResponseCreate>,
                            response: Response<ResponseCreate>
                        ) {
                            Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                            editor.putString(usernameK, temp_username)
                            editor.putString(passK, temp_password)
                            editor.apply()
                            finish()
                        }

                        override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {

                        }

                    })
                }
            }
        }
    }

    fun getUser(){

        val token_auth = "Bearer ${prefManager.getToken()}"
        val username_shp = prefManager.getUsername()

        server.instances.getDataUser(token_auth, username_shp).enqueue(object : Callback<ResponseDataUsers>{
            override fun onResponse(
                call: Call<ResponseDataUsers>,
                response: Response<ResponseDataUsers>
            ) {
                if (response.isSuccessful){
                    listUsers.clear()
                    response.body()?.let { listUsers.addAll(it.data) }

                    if (listUsers.size == 1){
                        binding.inputEmail.editText?.setText(listUsers[0].email)
                        binding.inputUsername.editText?.setText(listUsers[0].username)
                        binding.inputTlp.editText?.setText(listUsers[0].noTelepon)
                        penampung_uID = listUsers[0].idU
                    }else{
                        for (item in listUsers){
                            if (item.username == username_shp){
                                binding.inputEmail.editText?.setText(item.email)
                                binding.inputUsername.editText?.setText(item.username)
                                binding.inputTlp.editText?.setText(item.noTelepon)
                                penampung_uID = item.idU
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataUsers>, t: Throwable) {

            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}