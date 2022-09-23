package com.example.tubes1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername_lgn: TextInputLayout
    private lateinit var inputPassword_lgn: TextInputLayout
    //lateinit var temp_regBundle: Bundle

    lateinit var penampung_username: String
    lateinit var penampung_password: String

    //database
    val dbU by lazy { UserDB(this) }

    //Shared Preference for login and register
    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        inputUsername_lgn = findViewById(R.id.username)
        inputPassword_lgn = findViewById(R.id.password)
        val btnBack_ly2: ImageView = findViewById(R.id.btnBack_ly2)
        val sign_up_click: Button = findViewById(R.id.register_click)
        val btnLogin_click: Button = findViewById(R.id.btnLogin)

        getBundle()
        penampung_username=""
        penampung_password=""

        sign_up_click.setOnClickListener {
            val move_to_register1 = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(move_to_register1)
        }

        btnBack_ly2.setOnClickListener {
            val move_to_main2 = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(move_to_main2)
        }
        btnLogin_click.setOnClickListener (View.OnClickListener {
            var cekLogin = false
            //mencari data dari server
            getPembanding(inputUsername_lgn.editText?.text.toString())

            val username: String = inputUsername_lgn.getEditText()?.getText().toString()
            val password: String = inputPassword_lgn.getEditText()?.getText().toString()
            // Toast.makeText(applicationContext, inputUsername_lgn.editText?.text.toString(), Toast.LENGTH_LONG).show()

            if(username.isEmpty()){
                inputUsername_lgn.setError("Username Tidak Boleh Kosong")
                cekLogin = false
            }

            if(password.isEmpty()){
                inputPassword_lgn.setError("Password Tidak Boleh Kosong")
                cekLogin = false
            }

            if((username == "admin" && password == "10778") || (username == penampung_username && password == penampung_password)){
                cekLogin = true
                var strUserName: String = penampung_username
                var strPass: String = penampung_password
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, strUserName)
                editor.putString(passK, strPass)
                editor.apply()
            }else{
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle("Salah Password atau Username")
                builder.setMessage("Isikan Password dan Usrname yang benar!!")
                    .setPositiveButton("Yes"){ dialog, which ->
                    }
                    .show()
            }
            if(!cekLogin)return@OnClickListener
            val move_to_home = Intent(this@LoginActivity, MainMenuActivity::class.java)
            startActivity(move_to_home)
        })
    }

    fun getBundle(){

        //preference
        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesRegister!!.contains(usernameK)){
            inputUsername_lgn.getEditText()?.setText(sharedPreferencesRegister!!.getString(usernameK, ""))
        }
        if (sharedPreferencesRegister!!.contains(passK)){
            inputPassword_lgn.getEditText()?.setText(sharedPreferencesRegister!!.getString(passK, ""))
        }
    }

    fun getPembanding(str: String){
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbU.userDao().getUser(str)[0]
            penampung_username = user.username
            penampung_password = user.password
            Toast.makeText(applicationContext, user.username, Toast.LENGTH_SHORT).show()
        }

    }
}