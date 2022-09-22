package com.example.tubes1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername_lgn: TextInputLayout
    private lateinit var inputPassword_lgn: TextInputLayout
    lateinit var temp_regBundle: Bundle

    lateinit var penampung_username: String
    lateinit var penampung_password: String
    lateinit var penampung_email: String
    lateinit var penampung_tgLahir: String
    lateinit var penampung_noTlp: String

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
                val username: String = inputUsername_lgn.getEditText()?.getText().toString()
                val password: String = inputPassword_lgn.getEditText()?.getText().toString()

                if(username.isEmpty()){
                    inputUsername_lgn.setError("Username Tidak Boleh Kosong")
                    cekLogin = false
                }

                if(password.isEmpty()){
                    inputPassword_lgn.setError("Password Tidak Boleh Kosong")
                    cekLogin = false
                }

                if((username == "admin" && password == "10778") || username == penampung_username && password == penampung_password && !penampung_username.isEmpty() && !penampung_password.isEmpty()){
                    cekLogin = true
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
        //memanggil bundle
        temp_regBundle = intent.getBundleExtra("register")!!
        //ambil data bundle
        penampung_username = temp_regBundle.getString("username")!!
        penampung_password = temp_regBundle.getString("password")!!
        penampung_email = temp_regBundle.getString("email")!!
        penampung_tgLahir = temp_regBundle.getString("tglLahir")!!
        penampung_noTlp = temp_regBundle.getString("tlp")!!
        inputUsername_lgn.getEditText()?.setText(penampung_username)
    }


}