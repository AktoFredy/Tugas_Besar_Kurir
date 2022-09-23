package com.example.tubes1

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.tubes1.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var cal = Calendar.getInstance()

//    var binding: ActivityRegisterBinding? = null
    val dbU by lazy { UserDB(this) }
    private var userId: Int = 0
    private lateinit var binding: ActivityRegisterBinding

    //Shared Preference for login and register
    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        //binding view==============
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(binding?.root)
        //==========================

        val move_to_login = Intent(this, LoginActivity::class.java)
//        val regBundle = Bundle()
//        regBundle.putString("username", "")
//        regBundle.putString("password", "")
//        regBundle.putString("email", "")
//        regBundle.putString("tglLahir", "")
//        regBundle.putString("tlp", "")
//        move_to_login.putExtra("register", regBundle)

        //preference
            sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
//        if (sharedPreferencesRegister!!.contains(usernameK)){
//            editTextName?.setText(sharedPreferencesRegister!!.getString(usernameK, ""))
//        }
//        if (sharedPreferencesRegister!!.contains(passK)){
//            editTextEmail?.setText(sharedPreferencesRegister!!.getString(passK, ""))
//        }

        // untuk datePicker
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            }
        }

        //menggunakan inputLayout yang diset click
        binding?.tglEdt?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        binding?.loginClick?.setOnClickListener {
            startActivity(move_to_login)
        }

        binding?.btnBackLy1?.setOnClickListener{
            val move_to_main1 = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(move_to_main1)
        }

        binding?.btnRegis?.setOnClickListener (View.OnClickListener{
            var checkRegister = false

            val temp_username: String = binding?.inputUsername?.getEditText()?.getText().toString()
            val temp_password: String = binding?.inputPassword1?.getEditText()?.getText().toString()
            val temp_confirmPass: String = binding?.inputConfirmPassword?.getEditText()?.getText().toString()
            val temp_email: String = binding?.inputEmail?.getEditText()?.getText().toString()
            val temp_tglLahir: String = binding?.inputTanggalLahir?.getEditText()?.getText().toString()
            val temp_noTlp: String = binding?.inputTlp?.getEditText()?.getText().toString()

            if(temp_username.isEmpty()){
                binding?.inputUsername?.setError("Username Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_password.isEmpty()){
                binding?.inputPassword1?.setError("Password Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_confirmPass.isEmpty()){
                binding?.inputConfirmPassword?.setError("Password Confirm Tidak Boleh Kosong")
                checkRegister = false
            }else if(temp_password != temp_confirmPass){
                binding?.inputConfirmPassword?.setError("Password Confirm Harus Sama")
                checkRegister = false
            }

            if(temp_email.isEmpty()){
                binding?.inputEmail?.setError("Email Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_tglLahir.isEmpty()){
                binding?.inputTanggalLahir?.setError("Tanggal Lahir Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_noTlp.isEmpty()){
                binding?.inputTlp?.setError("No Tlp Tidak Boleh Kosong")
                checkRegister = false
            }else if(temp_noTlp.length < 12){
                binding?.inputTlp?.setError("Panjang No Tlp harus > 12")
                checkRegister = false
            }

            if(!temp_username.isEmpty() && !temp_password.isEmpty() && !temp_confirmPass.isEmpty() && !temp_email.isEmpty() && !temp_tglLahir.isEmpty() && !temp_noTlp.isEmpty() && temp_noTlp.length == 12 && temp_password.equals(temp_confirmPass)){
                checkRegister = true

                CoroutineScope(Dispatchers.IO).launch {
                    dbU.userDao().addUser(
                        User(0, temp_email, temp_username, temp_password, temp_tglLahir, temp_noTlp)
                    )
                    finish()
                }

                //save to shareP
                var strUserName: String = binding.inputUsername.editText?.text.toString().trim()
                var strPass: String = binding.inputPassword1.editText?.text.toString().trim()
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, strUserName)
                editor.putString(passK, strPass)
                editor.apply()
            }

            if(!checkRegister)return@OnClickListener
            startActivity(move_to_login)
        })
    }
    private fun updateEditText(){
        var temp : String
        val formatku = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(formatku, Locale.US)
        temp = sdf.format(cal.getTime())
        binding?.inputTanggalLahir?.getEditText()?.setText(temp)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        binding = null
//    }
}